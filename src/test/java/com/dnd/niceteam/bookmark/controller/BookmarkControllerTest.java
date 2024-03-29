package com.dnd.niceteam.bookmark.controller;

import com.dnd.niceteam.bookmark.dto.BookmarkCreation;
import com.dnd.niceteam.bookmark.dto.BookmarkDeletion;
import com.dnd.niceteam.bookmark.service.BookmarkService;
import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.bookmark.dto.LectureBookmarkDto;
import com.dnd.niceteam.domain.bookmark.dto.LectureTimeDto;
import com.dnd.niceteam.domain.bookmark.dto.SideBookmarkDto;
import com.dnd.niceteam.domain.code.DayOfWeek;
import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
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

import java.time.LocalDate;
import java.time.LocalTime;
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
    @DisplayName("강의 북마크 페이지 조회 API")
    void lectureBookmarkPage() throws Exception {
        // given
        LectureBookmarkDto lectureBookmarkDto1 = new LectureBookmarkDto(
                1L,
                2L,
                3L,
                "모집글1",
                LocalDate.of(2022, 3, 1),
                0,
                0,
                0,
                "강의1"
        );
        lectureBookmarkDto1.setLectureTimes(List.of(
                new LectureTimeDto(3L, DayOfWeek.MON, LocalTime.of(1, 30)),
                new LectureTimeDto(3L, DayOfWeek.WED, LocalTime.of(3, 0))
        ));
        Pagination<LectureBookmarkDto> givenPagination = Pagination.<LectureBookmarkDto>builder()
                .page(0)
                .perSize(10)
                .totalCount(1)
                .contents(List.of(lectureBookmarkDto1))
                .build();
        given(mockBookmarkService.getLectureBookmarkPageByUsername(any(Pageable.class), anyString()))
                .willReturn(givenPagination);

        // expected
        mockMvc.perform(get("/bookmarks/lecture")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andDo(document("bookmarks-lecture-page",
                        requestParameters(
                                parameterWithName("page").description("페이지 인덱스"),
                                parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(
                                beneathPath("data.contents").withSubsectionId("data"),
                                fieldWithPath("id").description("북마크 ID"),
                                fieldWithPath("recruitingId").description("모집글 ID"),
                                fieldWithPath("projectId").description("프로젝트 ID"),
                                fieldWithPath("title").description("모집글 제목"),
                                fieldWithPath("recruitingEndDate").description("모집 마감 일자"),
                                fieldWithPath("commentCount").description("댓글 수"),
                                fieldWithPath("bookmarkCount").description("북마크 수"),
                                fieldWithPath("recruitingMemberCount").description("모집 인원"),
                                fieldWithPath("lectureName").description("강의명"),
                                fieldWithPath("lectureTimes").description("강의 일정 - 없을 경우 빈 리스트 반환"),
                                fieldWithPath("lectureTimes[].projectId").description("프로젝트 ID"),
                                fieldWithPath("lectureTimes[].dayOfWeek.title").description("강의 요일"),
                                fieldWithPath("lectureTimes[].dayOfWeek.code").description("강의 요일 코드"),
                                fieldWithPath("lectureTimes[].startTime").description("강의 시작 시간")
                        )
                ))
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("사이드 북마크 페이지 조회 API")
    void sideBookmarkPage() throws Exception {
        // given
        SideBookmarkDto sideBookmarkDto = new SideBookmarkDto(
                1L,
                2L,
                3L,
                "모집글1",
                LocalDate.of(2022, 3, 1),
                0,
                0,
                0,
                Field.IT_SW_GAME,
                FieldCategory.EXTRA_ACTIVITY
        );
        Pagination<SideBookmarkDto> givenPagination = Pagination.<SideBookmarkDto>builder()
                .page(0)
                .perSize(10)
                .totalCount(1)
                .contents(List.of(sideBookmarkDto))
                .build();
        given(mockBookmarkService.getSideBookmarkPageByUsername(any(Pageable.class), anyString()))
                .willReturn(givenPagination);

        // expected
        mockMvc.perform(get("/bookmarks/side")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andDo(document("bookmarks-side-page",
                        requestParameters(
                                parameterWithName("page").description("페이지 인덱스"),
                                parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(
                                beneathPath("data.contents").withSubsectionId("data"),
                                fieldWithPath("id").description("북마크 ID"),
                                fieldWithPath("recruitingId").description("모집글 ID"),
                                fieldWithPath("projectId").description("프로젝트 ID"),
                                fieldWithPath("title").description("모집글 제목"),
                                fieldWithPath("recruitingEndDate").description("모집 마감 일자"),
                                fieldWithPath("commentCount").description("댓글 수"),
                                fieldWithPath("bookmarkCount").description("북마크 수"),
                                fieldWithPath("recruitingMemberCount").description("모집 인원"),
                                fieldWithPath("field.code").description("분야 코드"),
                                fieldWithPath("field.title").description("분야"),
                                fieldWithPath("fieldCategory.code").description("분야 카테고리 코드"),
                                fieldWithPath("fieldCategory.title").description("분야 카테고리")
                        )
                ))
        ;
    }
}