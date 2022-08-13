package com.dnd.niceteam.comment.controller;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.comment.service.CommentService;
import com.dnd.niceteam.comment.CommentTestFactory;
import com.dnd.niceteam.common.RestDocsConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.dnd.niceteam.comment.CommentTestFactory.RECRUITING_ID;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    @DisplayName("댓글 등록 API를 요청합니다.")
    public void commentCreate() throws Exception {
        //given
        CommentCreation.RequestDto request = CommentTestFactory.createCommentRequest();
        CommentCreation.ResponseDto response = CommentTestFactory.createCommentResponse();
        when(commentService.addComment(RECRUITING_ID, request)).thenReturn(response);

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
                                requestFields(
                                        fieldWithPath("memberId").description("회원 식별자 ID"),
                                        fieldWithPath("content").description("댓글 내용")
                                                .attributes(key("constraint").value("최대 255자")),
                                        fieldWithPath("parentId").description("모댓글 ID")
                                                .attributes(key("constraint").value("모댓글인 경우 0으로 요청"))
                                        ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("id").description("댓글 식별자 ID")
                                )
                        )
                );
    }
}