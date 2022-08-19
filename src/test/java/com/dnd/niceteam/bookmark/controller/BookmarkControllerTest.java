package com.dnd.niceteam.bookmark.controller;

import com.dnd.niceteam.bookmark.dto.BookmarkCreation;
import com.dnd.niceteam.bookmark.dto.BookmarkDeletion;
import com.dnd.niceteam.bookmark.dto.BookmarkDto;
import com.dnd.niceteam.bookmark.service.BookmarkService;
import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.error.exception.ErrorCode;
import com.dnd.niceteam.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(controllers = BookmarkController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookmarkService mockBookmarkService;

    @Test
    @WithMockUser
    @DisplayName("북마크 생성 API")
    void bookmarkCreate() throws Exception {
        // given
        BookmarkCreation.ResponseDto responseDto = new BookmarkCreation.ResponseDto();
        responseDto.setId(1L);
        given(mockBookmarkService.createBookmark(anyString(), anyLong())).willReturn(responseDto);

        // expected
        mockMvc.perform(post("/bookmarks/{recruitingId}", 1L)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andDo(document("bookmark-create",
                        pathParameters(
                                parameterWithName("recruitingId").description("모집글 ID")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").description("북마크 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("북마크 삭제 API")
    void bookmarkDelete() throws Exception {
        // given
        long givenBookmarkId = 1L;
        BookmarkDeletion.ResponseDto responseDto = new BookmarkDeletion.ResponseDto();
        responseDto.setId(givenBookmarkId);
        given(mockBookmarkService.isBookmarkOwnedByMember(eq(givenBookmarkId), anyString())).willReturn(true);
        given(mockBookmarkService.deleteBookmark(givenBookmarkId)).willReturn(responseDto);

        // expected
        mockMvc.perform(delete("/bookmarks/{bookmarkId}", givenBookmarkId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andDo(document("bookmark-delete",
                        pathParameters(
                                parameterWithName("bookmarkId").description("북마크 ID")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").description("북마크 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("북마크 삭제 API - 소유하지 않은 북마크")
    void bookmarkDelete_BookmarkNotOwned() throws Exception {
        // given
        given(mockBookmarkService.isBookmarkOwnedByMember(anyLong(), anyString())).willReturn(false);

        // expected
        mockMvc.perform(delete("/bookmarks/{bookmarkId}", 1L)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.BOOKMARK_NOT_OWNED.name()))
                .andExpect(jsonPath("$.error.message").value(ErrorCode.BOOKMARK_NOT_OWNED.getMessage()))
                .andExpect(jsonPath("$.error.errors").isEmpty())
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("북마크 페이지 조회 API")
    void bookmarkPage() throws Exception {
        // given
        BookmarkDto givenBookmarkDto = new BookmarkDto();
        givenBookmarkDto.setId(1L);
        givenBookmarkDto.setRecruitingId(2L);
        givenBookmarkDto.setRecruitingTitle("테스트 모집글 제목");
        Pagination<BookmarkDto> givenPagination = Pagination.<BookmarkDto>builder()
                .page(0)
                .perSize(10)
                .totalCount(1)
                .contents(List.of(givenBookmarkDto))
                .build();
        given(mockBookmarkService.getBookmarkPageByUsername(any(Pageable.class), anyString()))
                .willReturn(givenPagination);

        // expected
        mockMvc.perform(get("/bookmarks")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andDo(document("bookmarks-page",
                        requestParameters(
                                parameterWithName("page").description("페이지 인덱스"),
                                parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(
                                beneathPath("data.contents").withSubsectionId("data"),
                                fieldWithPath("id").description("북마크 ID"),
                                fieldWithPath("recruitingId").description("모집글 ID"),
                                fieldWithPath("recruitingTitle").description("모집글 제목")
                        )
                ))
                ;
    }
}