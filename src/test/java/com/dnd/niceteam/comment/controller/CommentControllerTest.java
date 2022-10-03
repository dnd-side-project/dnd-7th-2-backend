package com.dnd.niceteam.comment.controller;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.comment.dto.CommentFind;
import com.dnd.niceteam.comment.dto.CommentModify;
import com.dnd.niceteam.comment.service.CommentService;
import static com.dnd.niceteam.comment.DtoFactoryForTest.*;
import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.common.jackson.RestDocsObjectMapper;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.comment.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ RestDocsConfig.class, RestDocsObjectMapper.class })
@AutoConfigureRestDocs
@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestDocsObjectMapper objectMapper;
    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    @DisplayName("댓글 등록 요청 API")
    public void commentCreate() throws Exception {
        //given
        CommentCreation.RequestDto request = createCommentAddRequest();
        CommentCreation.ResponseDto response = createCommentAddResponse();
        when(commentService.addComment(anyLong(), anyString(), eq(request))).thenReturn(response);

        //then
        mockMvc.perform(
                post("/recruiting/{recruitingId}/comment", RECRUITING_ID)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("comment-create",
                                pathParameters(
                                        parameterWithName("recruitingId").description("모집글 식별자(ID)")
                                ),
                                requestFields(
                                        fieldWithPath("content").description("댓글 내용")
                                                .attributes(key("constraint").value("최대 255자")),
                                        fieldWithPath("parentId").description("모댓글 ID")
                                                .attributes(key("constraint").value("모댓글인 경우 0으로 요청"))
                                        ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("id").description("댓글 식별자 ID"),
                                        fieldWithPath("parentId").description("모댓글 식별자 ID"),
                                        fieldWithPath("groupNo").description("댓글 그룹 번호(하나의 모댓글과 해당 모댓글의 답글들은 동일한 그룹 번호를 갖습니다.)")
                                )
                        ));
    }

    @Test
    @WithMockUser
    @DisplayName("모집글의 댓글 목록 조회 요청 API")
    public void commentList() throws Exception {
        //given
        CommentFind.ResponseDto response = createCommentListResponse();
        response.setNickname("writer-nickname");
        when(commentService.getComments(anyInt(), anyInt(), anyLong(), eq(null))).thenReturn(
                Pagination.<CommentFind.ResponseDto>builder()
                        .page(PAGE-1)
                        .perSize(PER_SIZE)
                        .contents(List.of(response))
                        .totalCount(1)
                        .build());

        //then
        mockMvc.perform(get("/recruiting/{recruitingId}/comment", RECRUITING_ID)
                                .accept(MediaType.APPLICATION_JSON)
                                .param("page", "1")
                                .param("perSize", "10")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("comment-list",
                                pathParameters(
                                        parameterWithName("recruitingId").description("모집글 식별자(ID)")
                                ),
                                requestParameters(
                                        parameterWithName("page").description("현재 페이지(입력하지 않을 경우, 1)").optional(),
                                        parameterWithName("perSize").description("페이지 별 아이템 개수(입력하지 않을 경우, 10)").optional()
                                ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("page").description("현재 페이지"),
                                        fieldWithPath("perSize").description("페이지 아이템 개수"),
                                        fieldWithPath("totalCount").description("총 아이템 개수"),
                                        fieldWithPath("totalPages").description("총 페이지 개수"),
                                        fieldWithPath("prev").description("이전 페이지 존재 여부"),
                                        fieldWithPath("next").description("다음 페이지 존재 여부"),

                                        fieldWithPath("contents[].commentId").description("댓글 식별자(ID)"),
                                        fieldWithPath("contents[].parentId").description("모댓글 식별자(ID)"),
                                        fieldWithPath("contents[].recruitingId").description("모집글 식별자(ID)"),
                                        fieldWithPath("contents[].content").description("댓글 내용"),
                                        fieldWithPath("contents[].createdAt").description("생성 날짜(요일+시간)"),

                                        fieldWithPath("contents[].nickname").description("댓글 작성자 닉네임")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    @DisplayName("내가 쓴 댓글 목록 조회 요청 API")
    public void myCommentList() throws Exception {
        //given
        CommentFind.ResponseDto response = createCommentListResponse();
        response.setRecruitingTitle("모집글 제목");
        response.setRecruitingType(Type.LECTURE);
        when(commentService.getComments(anyInt(), anyInt(), eq(null), anyString())).thenReturn(
                Pagination.<CommentFind.ResponseDto>builder()
                        .page(PAGE-1)
                        .perSize(PER_SIZE)
                        .contents(List.of(response))
                        .totalCount(1).build());
        //then
        mockMvc.perform(
                        get("/recruiting/comment/me")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("page", "1")
                                .param("perSize", "10")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("my-comment-list",
                                requestParameters(
                                        parameterWithName("page").description("현재 페이지(입력하지 않을 경우, 1)").optional(),
                                        parameterWithName("perSize").description("페이지 별 아이템 개수(입력하지 않을 경우, 10)").optional()
                                ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("page").description("현재 페이지"),
                                        fieldWithPath("perSize").description("페이지 아이템 개수"),
                                        fieldWithPath("totalCount").description("총 아이템 개수"),
                                        fieldWithPath("totalPages").description("총 페이지 개수"),
                                        fieldWithPath("prev").description("이전 페이지 존재 여부"),
                                        fieldWithPath("next").description("다음 페이지 존재 여부"),

                                        fieldWithPath("contents[].commentId").description("댓글 식별자(ID)"),
                                        fieldWithPath("contents[].parentId").description("모댓글 식별자(ID)"),
                                        fieldWithPath("contents[].content").description("댓글 내용"),
                                        fieldWithPath("contents[].recruitingId").description("모집글 식별자(ID)"),
                                        fieldWithPath("contents[].createdAt").description("생성 날짜(요일+시간)"),

                                        fieldWithPath("contents[].recruitingTitle").description("모집글 제목").type(String.class),
                                        fieldWithPath("contents[].recruitingType").description("모집글 타입").type(Type.class)
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 제거 요청 API")
    public void commentRemove() throws Exception {
        // given
        Comment mockComment = mock(Comment.class, RETURNS_DEEP_STUBS);
        when(mockComment.getParentId()).thenReturn(PARENT_ID);
        doNothing().when(commentService).removeComment(eq(COMMENT_ID), anyString());

        // when
        ResultActions result = mockMvc.perform(delete("/recruiting/comment/{commentId}", COMMENT_ID)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        );
        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("comment-remove",
                        pathParameters(
                                parameterWithName("commentId").description("댓글 식별자(ID)")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 요청 API")
    public void commentModify() throws Exception {
        // given
        Comment mockComment = mock(Comment.class, RETURNS_DEEP_STUBS);
        when(mockComment.getId()).thenReturn(COMMENT_ID);

        CommentModify.RequestDto requestDto = new CommentModify.RequestDto();
        requestDto.setId(COMMENT_ID);
        requestDto.setContent("업데이트된 댓글 내용입니다.");
        CommentModify.ResponseDto responseDto = new CommentModify.ResponseDto();
        responseDto.setId(COMMENT_ID);
        responseDto.setParentId(PARENT_ID);
        responseDto.setGroupNo(1L);

        when(commentService.modifyComment(eq(requestDto), anyString())).thenReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(put("/recruiting/comment")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        );
        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("comment-modify",
                        requestFields(
                                fieldWithPath("id").description("수정할 댓글 식별자(ID)"),
                                fieldWithPath("content").description("수정된 댓글 내용")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").description("댓글 식별자(ID)"),
                                fieldWithPath("parentId").description("모댓글 식별자(ID)"),
                                fieldWithPath("groupNo").description("댓글 그룹 번호")
                        )
                ));
    }
}