package com.dnd.niceteam.recruiting.controller;

import com.dnd.niceteam.comment.DtoFactoryForTest;
import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.common.jackson.RestDocsObjectMapper;
import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.dto.RecruitingFind;
import com.dnd.niceteam.recruiting.dto.RecruitingModify;
import com.dnd.niceteam.recruiting.service.RecruitingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.dnd.niceteam.comment.DtoFactoryForTest.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
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
@WebMvcTest(controllers = RecruitingController.class)
class RecruitingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestDocsObjectMapper objectMapper;

    @MockBean
    private RecruitingService recruitingService;
    @Mock
    private RecruitingRepository recruitingRepository;

    @Test
    @WithMockUser
    @DisplayName("(강의) 모집글 등록 요청 API")
    public void createRecruiting() throws Exception {
        // given
        RecruitingCreation.RequestDto requestDto = DtoFactoryForTest.createLectureRecruitingRequest();
        RecruitingCreation.ResponseDto responseDto = DtoFactoryForTest.createLectureRecruitingResponse();
        when(recruitingService.addProjectAndRecruiting(anyString(), eq(requestDto))).thenReturn(responseDto);

        // then
        mockMvc.perform(
                post("/recruiting")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("recruiting-create",
                                requestFields(
                                        fieldWithPath("title").description("모집글 제목"),
                                        fieldWithPath("content").description("모집글 내용"),
                                        fieldWithPath("recruitingMemberCount").description("모집 인원"),
                                        fieldWithPath("recruitingType").description("모집글 타입"),
                                        fieldWithPath("activityArea.code").description("활동 지역 코드"),
                                        fieldWithPath("activityArea.title").description("활동 지역"),
                                        fieldWithPath("introLink").description("프로젝트 소개(없다면 null이 아닌 빈 문자열)"),
                                        fieldWithPath("status.code").description("모집글 상태 코드"),
                                        fieldWithPath("status.title").description("모집글 상태"),
                                        fieldWithPath("recruitingEndDate").description("모집 마감일")
                                                .attributes(key("constraint")
                                                        .value("must be a date in the present or in the future")),
                                        fieldWithPath("personalityAdjectives[].code").description("배열 형태의 선호하는 성향 형용사 코드"),
                                        fieldWithPath("personalityAdjectives[].title").description("배열 형태의 선호하는 성향 형용사"),
                                        fieldWithPath("personalityNouns[].code").description("배열 형태의 선호하는 성향 명사 코드"),
                                        fieldWithPath("personalityNouns[].title").description("배열 형태의 선호하는 성향 명사"),
                                        fieldWithPath("projectStartDate").description("프로젝트 시작일"),
                                        fieldWithPath("projectEndDate").description("프로젝트 종료일"),
                                        fieldWithPath("projectName").description("강의명 혹은 사이드 프로젝트명"),
                                        fieldWithPath("activityDayTimes").description("배열 형태의 활동 요일 및 시간"),
                                        fieldWithPath("activityDayTimes[].dayOfWeek.code").description("활동 요일 코드"),
                                        fieldWithPath("activityDayTimes[].dayOfWeek.title").description("활동 요일"),
                                        fieldWithPath("activityDayTimes[].startTime").description("활동 시작 시간"),
                                        fieldWithPath("activityDayTimes[].endTime").description("활동 종료 시간"),

                                        fieldWithPath("lectureTimes").description("배열 형태의 강의 요일 및 시간").optional(),
                                        fieldWithPath("lectureTimes[].dayOfWeek.code").description("강의 요일 코드"),
                                        fieldWithPath("lectureTimes[].dayOfWeek.title").description("강의 요일"),
                                        fieldWithPath("lectureTimes[].startTime").description("강의 시간"),
                                        fieldWithPath("professor").description("교수명").optional(),
                                        fieldWithPath("departmentId").description("학과 식별자(ID)").optional(),

                                        fieldWithPath("field").description("분야").optional(),
                                        fieldWithPath("fieldCategory").description("분야 카테고리").optional()
                                ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("recruitingId").description("모집글 식별자(ID)"),
                                        fieldWithPath("projectId").description("프로젝트 식별자(ID)")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    @DisplayName("모집글 상세 조회 요청 API")
    public void getRecruitingDetail() throws Exception {
        // given
        RecruitingFind.DetailResponseDto responseDto = DtoFactoryForTest.createDetailSideRecruitingResponse();
        when(recruitingService.getRecruiting(anyLong(), anyString())).thenReturn(responseDto);

        // then
        mockMvc.perform(get("/recruiting/{recruitingId}", RECRUITING_ID)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))

                .andDo(
                        document("recruiting-detail",
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("memberNickname").description("프로젝트 식별자(ID)"),
                                        fieldWithPath("title").description("모집글 제목"),
                                        fieldWithPath("content").description("모집글 내용"),
                                        fieldWithPath("recruitingType").description("모집글 타입"),
                                        fieldWithPath("recruitingStatus.code").description("모집글 상태 코드"),
                                        fieldWithPath("recruitingStatus.title").description("모집글 상태"),
                                        fieldWithPath("commentCount").description("댓글 개수"),
                                        fieldWithPath("bookmarkCount").description("북마크 개수"),
                                        fieldWithPath("introLink").description("프로젝트 소개"),
                                        fieldWithPath("recruitingMemberCount").description("모집 인원"),
                                        fieldWithPath("activityArea.code").description("활동 지역 코드"),
                                        fieldWithPath("activityArea.title").description("활동 지역"),
                                        fieldWithPath("personalityAdjectives[].code").description("배열 형태의 선호하는 성향 형용사 코드"),
                                        fieldWithPath("personalityAdjectives[].title").description("배열 형태의 선호하는 성향 형용사"),
                                        fieldWithPath("personalityNouns[].code").description("배열 형태의 선호하는 성향 명사 코드"),
                                        fieldWithPath("personalityNouns[].title").description("배열 형태의 선호하는 성향 명사"),
                                        fieldWithPath("recruitingCreatedDate").description("모집글 생성 일자"),
                                        fieldWithPath("recruitingEndDate").description("모집 마감일"),
                                        fieldWithPath("activityDayTimes").description("배열 형태의 활동 요일 및 시간"),
                                        fieldWithPath("activityDayTimes[].dayOfWeek.code").description("활동 요일 코드"),
                                        fieldWithPath("activityDayTimes[].dayOfWeek.title").description("활동 요일"),
                                        fieldWithPath("activityDayTimes[].startTime").description("활동 시작 시간"),
                                        fieldWithPath("activityDayTimes[].endTime").description("활동 종료 시간"),
                                        fieldWithPath("isBookmarked").description("사용자의 모집글 북마크 여부"),

                                        fieldWithPath("projectResponse.id").description("프로젝트 식별자(ID)"),
                                        fieldWithPath("projectResponse.name").description("강의명 혹은 사이드 프로젝트명"),
                                        fieldWithPath("projectResponse.type").description("프로젝트 타입"),
                                        fieldWithPath("projectResponse.startDate").description("프로젝트 시작일"),
                                        fieldWithPath("projectResponse.endDate").description("프로젝트 종료일"),
                                        fieldWithPath("projectResponse.status").description("프로젝트 상태"),

                                        fieldWithPath("projectResponse.professor").description("강의 교수명").optional(),
                                        fieldWithPath("projectResponse.lectureTimes").description("배열 형태의 강의 요일 및 시간").optional(),
                                        fieldWithPath("projectResponse.lectureTimes[].dayOfWeek").type(JsonFieldType.STRING).description("강의 요일").optional(),
                                        fieldWithPath("projectResponse.lectureTimes[].startTime").type(JsonFieldType.OBJECT).description("강의 시간(LocalTime)").optional(),
                                        fieldWithPath("projectResponse.department").description("학과 데이터").optional(),
                                        fieldWithPath("projectResponse.department.id").description("학과 식별자").type(JsonFieldType.NUMBER).optional(),
                                        fieldWithPath("projectResponse.department.collegeName").description("단과대학 이름").type(JsonFieldType.STRING).optional(),
                                        fieldWithPath("projectResponse.department.name").description("학과 이름").type(JsonFieldType.STRING).optional(),
                                        fieldWithPath("projectResponse.department.mainBranchType").description("주 캠퍼스 여부").type(JsonFieldType.STRING).optional(),
                                        fieldWithPath("projectResponse.department.region").description("지역").type(JsonFieldType.STRING).optional(),
                                        fieldWithPath("projectResponse.department.university.id").description("대학교 식별자").type(JsonFieldType.NUMBER).optional(),
                                        fieldWithPath("projectResponse.department.university.name").description("대학교 이름").type(JsonFieldType.STRING).optional(),
                                        fieldWithPath("projectResponse.department.university.emailDomain").description("대학교 이메일 도메인").type(JsonFieldType.STRING).optional(),

                                        fieldWithPath("projectResponse.field.code").description("분야 코드").optional(),
                                        fieldWithPath("projectResponse.field.title").description("분야").optional(),
                                        fieldWithPath("projectResponse.fieldCategory.code").description("분야 카테고리 코드").optional(),
                                        fieldWithPath("projectResponse.fieldCategory.title").description("분야 카테고리").optional(),

                                        fieldWithPath("projectResponse.createdDate").description("데이터 정보 - 프로젝트 데이터 생성 일자"),
                                        fieldWithPath("projectResponse.lastModifiedDate").description("데이터 정보 - 프로젝트 데이터 수정 일자"),
                                        fieldWithPath("projectResponse.createdBy").description("데이터 정보 - 프로젝트 데이터 생성자"),
                                        fieldWithPath("projectResponse.lastModifiedBy").description("데이터 정보 - 프로젝트 데이터 수정자")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    @DisplayName("내가 쓴글 목록 조회 요청 API")
    public void getMyRecruitings () throws Exception {
        // given
        RecruitingFind.ListResponseDto responseDto = DtoFactoryForTest.createListLectureRecruitingResponse();
        when(recruitingService.getMyRecruitings(anyInt(), anyInt(), any(RecruitingStatus.class), anyString())).thenReturn(
                Pagination.<RecruitingFind.ListResponseDto>builder()
                .page(PAGE-1)
                .perSize(PER_SIZE)
                .contents(List.of(responseDto))
                .totalCount(1).build());

        // then
        mockMvc.perform(get("/recruiting/me")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("perSize", "10")
                        .param("status", RecruitingStatus.IN_PROGRESS.name())
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))

                .andDo(
                        document("my-recruiting-list",
                                requestParameters(
                                        parameterWithName("page").description("현재 페이지(입력하지 않을 경우, 1)").optional(),
                                        parameterWithName("perSize").description("페이지 별 아이템 개수(입력하지 않을 경우, 10)").optional(),
                                        parameterWithName("status").description("모집글 상태(입력하지 않을 경우, 전체 조회").optional()
                                ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("page").description("현재 페이지"),
                                        fieldWithPath("perSize").description("페이지 아이템 개수"),
                                        fieldWithPath("totalCount").description("총 아이템 개수"),
                                        fieldWithPath("totalPages").description("총 페이지 개수"),
                                        fieldWithPath("prev").description("이전 페이지 존재 여부"),
                                        fieldWithPath("next").description("다음 페이지 존재 여부"),

                                        fieldWithPath("contents[].id").description("모집글 식별자(ID)"),
                                        fieldWithPath("contents[].title").description("모집글 제목"),
                                        fieldWithPath("contents[].type").description("모집글 타입"),
                                        fieldWithPath("contents[].status.code").description("모집글 상태 코드"),
                                        fieldWithPath("contents[].status.title").description("모집글 상태"),
                                        fieldWithPath("contents[].commentCount").description("댓글 개수"),
                                        fieldWithPath("contents[].bookmarkCount").description("북마크 개수"),
                                        fieldWithPath("contents[].projectName").description("강의명 혹은 사이드 프로젝트명"),
                                        fieldWithPath("contents[].createdDate").description("모집글 생성 일자"),

                                        fieldWithPath("contents[].professor").description("강의 교수명").optional(),

                                        fieldWithPath("contents[].field").description("분야").type(Field.class).optional(),
                                        fieldWithPath("contents[].fieldCategory").description("분야 카테고리").type(FieldCategory.class).optional()
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    @DisplayName("(사이드) 모집글 추천 요청 API")
    public void getRecommendedRecruitings () throws Exception {
        // given
        RecruitingFind.RecommendedListResponseDto responseDto = DtoFactoryForTest.createRecommendedSideRecruitingListResponse();
        when(recruitingService.getRecommendedRecruitings(anyInt(), anyInt(), anyString())).thenReturn(
                Pagination.<RecruitingFind.RecommendedListResponseDto>builder()
                        .page(PAGE-1)
                        .perSize(PER_SIZE)
                        .contents(List.of(responseDto))
                        .totalCount(1).build());

        // then
        mockMvc.perform(get("/recruiting/recommend-side")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("perSize", "4")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))

                .andDo(
                        document("recruiting-recommend-side",
                                requestParameters(
                                        parameterWithName("page").description("현재 페이지(작성하지 않을 경우, 1)").optional(),
                                        parameterWithName("perSize").description("페이지 아이템 개수(작성하지 않을 경우, 4)").optional()
                                ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("page").description("현재 페이지"),
                                        fieldWithPath("perSize").description("페이지 아이템 개수"),
                                        fieldWithPath("totalCount").description("총 아이템 개수"),
                                        fieldWithPath("totalPages").description("총 페이지 개수"),
                                        fieldWithPath("prev").description("이전 페이지 존재 여부"),
                                        fieldWithPath("next").description("다음 페이지 존재 여부"),

                                        fieldWithPath("contents[].recruitingId").description("모집글 식별자(ID)"),
                                        fieldWithPath("contents[].title").description("모집글 제목"),
                                        fieldWithPath("contents[].recruitingMemberCount").description("모집 인원"),
                                        fieldWithPath("contents[].field.code").description("분야 코드"),
                                        fieldWithPath("contents[].field.title").description("분야"),
                                        fieldWithPath("contents[].fieldCategory.code").description("분야 카테고리 코드"),
                                        fieldWithPath("contents[].fieldCategory.title").description("분야 카테고리"),
                                        fieldWithPath("contents[].projectStartDate").description("프로젝트 시작일"),
                                        fieldWithPath("contents[].projectEndDate").description("프로젝트 종료일"),
                                        fieldWithPath("contents[].recruitingEndDate").description("모집 마감일")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    @DisplayName("검색 목록 요청 API")
    public void getSearchRecruitings () throws Exception {
        //given
        RecruitingFind.ListResponseDto responseDto = createSearchSideListResponseDto();
        Pagination<RecruitingFind.ListResponseDto> recruitingPagination = Pagination.<RecruitingFind.ListResponseDto>builder()
                .page(PAGE-1)
                .perSize(PER_SIZE)
                .totalCount(1)
                .contents(List.of(responseDto))
                .build();
        when(recruitingService.getSearchRecruitings(anyInt(), anyInt(), any(Field.class), any(Type.class), anyString(), anyString()))
                .thenReturn(recruitingPagination);

        // then
        mockMvc.perform(get("/recruiting")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("perSize", "10")
                        .param("field", String.valueOf(Field.IT_SW_GAME))
                        .param("type", String.valueOf(Type.SIDE))
                        .param("searchWord", "검색 키워드")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))

                .andDo(
                        document("recruiting-search-list",
                                requestParameters(
                                        parameterWithName("page").description("현재 페이지(작성하지 않을 경우, 1)").optional(),
                                        parameterWithName("perSize").description("페이지 아이템 개수(작성하지 않을 경우, 10)").optional(),
                                        parameterWithName("field").description("필터링 선택한 분야 (SIDE 타입의 모집글 검색 화면에서 필터링 선택한 경우에만 입력. 입력하지 않으면 전체 분야 조회(필터링 X)").optional(),
                                        parameterWithName("type").description("선택한 모집글 타입(SIDE 혹은 LECTURE)"),
                                        parameterWithName("searchWord").description("검색 키워드(입력하지 않으면 기본값으로 조회)").optional()
                                ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("page").description("현재 페이지"),
                                        fieldWithPath("perSize").description("페이지 아이템 개수"),
                                        fieldWithPath("totalCount").description("총 아이템 개수"),
                                        fieldWithPath("totalPages").description("총 페이지 개수"),
                                        fieldWithPath("prev").description("이전 페이지 존재 여부"),
                                        fieldWithPath("next").description("다음 페이지 존재 여부"),

                                        fieldWithPath("contents[].id").description("모집글 식별자(ID)"),
                                        fieldWithPath("contents[].title").description("모집글 제목"),
                                        fieldWithPath("contents[].type").description("모집글 타입"),
                                        fieldWithPath("contents[].status.code").description("모집글 상태 코드"),
                                        fieldWithPath("contents[].status.title").description("모집글 상태"),
                                        fieldWithPath("contents[].commentCount").description("댓글 개수"),
                                        fieldWithPath("contents[].bookmarkCount").description("북마크 개수"),
                                        fieldWithPath("contents[].projectName").description("강의명 혹은 사이드 프로젝트명"),

                                        fieldWithPath("contents[].recruitingEndDate").description("모집글 마감 일자"),
                                        fieldWithPath("contents[].recruitingMemberCount").description("모집 인원"),
                                        fieldWithPath("contents[].recruiterNickname").description("모집글 작성자 닉네임"),

                                        fieldWithPath("contents[].professor").description("강의 교수명").type(JsonFieldType.STRING).optional(),

                                        fieldWithPath("contents[].field.code").description("분야 코드").optional(),
                                        fieldWithPath("contents[].field.title").description("분야").optional(),
                                        fieldWithPath("contents[].fieldCategory.code").description("분야 카테고리 코드").optional(),
                                        fieldWithPath("contents[].fieldCategory.title").description("분야 카테고리").optional()
                                )
                        )
                );
    }

    @Test
    @DisplayName("모집글 수정 요청 API")
    @WithMockUser
    public void modifyRecruiting() throws Exception {
        // given
        Long projectId = 1L;
        Recruiting mockRecruiting = mock(Recruiting.class, RETURNS_DEEP_STUBS);
        RecruitingModify.RequestDto requestDto = createRecruitingModifyRequest();
        when(mockRecruiting.getId()).thenReturn(RECRUITING_ID);

        RecruitingModify.ResponseDto responseDto = new RecruitingModify.ResponseDto();
        responseDto.setRecruitingId(RECRUITING_ID);
        responseDto.setProjectId(projectId);
        when(recruitingService.modifyProjectAndRecruiting(anyLong(), eq(requestDto), anyString())).thenReturn(responseDto);

        // expected
        mockMvc.perform(put("/recruiting/{recruitingId}", RECRUITING_ID)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("bookmark-delete",
                        requestFields(
                                fieldWithPath("title").description("모집글 제목"),
                                fieldWithPath("content").description("모집글 내용"),
                                fieldWithPath("recruitingMemberCount").description("모집 인원"),
                                fieldWithPath("recruitingType").description("모집글 타입"),
                                fieldWithPath("activityArea.code").description("활동 지역 코드"),
                                fieldWithPath("activityArea.title").description("활동 지역"),
                                fieldWithPath("introLink").description("프로젝트 소개(없다면 null이 아닌 빈 문자열)"),
                                fieldWithPath("recruitingEndDate").description("모집 마감일"),
                                fieldWithPath("personalityAdjectives[].code").description("배열 형태의 선호하는 성향 형용사 코드"),
                                fieldWithPath("personalityAdjectives[].title").description("배열 형태의 선호하는 성향 형용사"),
                                fieldWithPath("personalityNouns[].code").description("배열 형태의 선호하는 성향 명사 코드"),
                                fieldWithPath("personalityNouns[].title").description("배열 형태의 선호하는 성향 명사"),
                                fieldWithPath("projectStartDate").description("프로젝트 시작일"),
                                fieldWithPath("projectEndDate").description("프로젝트 종료일"),
                                fieldWithPath("projectName").description("강의명 혹은 사이드 프로젝트명"),
                                fieldWithPath("activityDayTimes").description("배열 형태의 활동 요일 및 시간"),
                                fieldWithPath("activityDayTimes[].dayOfWeek.code").description("활동 요일 코드"),
                                fieldWithPath("activityDayTimes[].dayOfWeek.title").description("활동 요일"),
                                fieldWithPath("activityDayTimes[].startTime").description("활동 시작 시간"),
                                fieldWithPath("activityDayTimes[].endTime").description("활동 종료 시간"),

                                fieldWithPath("lectureTimes").description("배열 형태의 강의 요일 및 시간").optional(),
                                fieldWithPath("lectureTimes[].dayOfWeek.code").description("강의 요일 코드"),
                                fieldWithPath("lectureTimes[].dayOfWeek.title").description("강의 요일"),
                                fieldWithPath("lectureTimes[].startTime").description("강의 시간"),
                                fieldWithPath("professor").description("교수명").optional(),
                                fieldWithPath("departmentId").description("학과 식별자(ID)").optional(),

                                fieldWithPath("field").description("분야").optional(),
                                fieldWithPath("fieldCategory").description("분야 카테고리").optional()
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("recruitingId").description("모집글 식별자(ID)"),
                                fieldWithPath("projectId").description("프로젝트 식별자(ID)")
                        )
                ));
    }

    @Test
    @DisplayName("모집글 제거 요청 API")
    @WithMockUser
    public void removeRecruiting() throws Exception {
        // given
        Recruiting mockRecruiting = mock(Recruiting.class, RETURNS_DEEP_STUBS);
        when(mockRecruiting.getId()).thenReturn(RECRUITING_ID);
        when(recruitingRepository.findById(anyLong())).thenReturn(Optional.of(mockRecruiting));
        doNothing().when(recruitingService).removeRecruiting(anyLong(), anyString());

        // expected
        mockMvc.perform(delete("/recruiting/{recruitingId}", RECRUITING_ID)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("recruiting-remove",
                        pathParameters(
                                parameterWithName("recruitingId").description("모집글 식별자(ID)")
                        )
                ));
    }

    @Test
    @DisplayName("모집글 끌올 요청 API")
    @WithMockUser
    public void poolupRecruiting() throws Exception {
        // given
        RecruitingModify.PoolUpRequestDto requestDto = new RecruitingModify.PoolUpRequestDto();
        requestDto.setPoolUpDate(LocalDateTime.now());
        // expected
        mockMvc.perform(put("/recruiting/{recruitingId}/pool-up", RECRUITING_ID)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("recruiting-poolup",
                        pathParameters(
                                parameterWithName("recruitingId").description("모집글 식별자(ID)")
                        ),
                        requestFields(
                                fieldWithPath("poolUpDate").description("모집글 끌올 요일 및 시간")
                        )
                ));
    }
}