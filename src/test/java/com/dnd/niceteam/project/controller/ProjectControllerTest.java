package com.dnd.niceteam.project.controller;

import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.project.ProjectTestFactory;
import com.dnd.niceteam.project.dto.LectureTimeResponse;
import com.dnd.niceteam.project.dto.ProjectMemberRequest;
import com.dnd.niceteam.project.dto.ProjectMemberResponse;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.project.service.ProjectService;
import com.dnd.niceteam.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(
        controllers = ProjectController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    @DisplayName("내 팀플 목록 조회")
    @Test
    @WithMockUser
    void getMyProjectList() throws Exception {
        // given
        ProjectResponse.ListItem projectListItem = mock(ProjectResponse.ListItem.class, RETURNS_DEEP_STUBS);
        when(projectListItem.getLectureTimes()).thenReturn(List.of(mock(LectureTimeResponse.class)));

        given(projectService.getProjectList(anyInt(), anyInt(), any(ProjectStatus.class), any(User.class))).willReturn(
                Pagination.<ProjectResponse.ListItem>builder()
                        .page(1)
                        .perSize(10)
                        .totalCount(1)
                        .contents(List.of(projectListItem))
                        .build()
        );

        // then
        mockMvc.perform(
                        get("/projects/me")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("page", "1")
                                .param("perSize", "10")
                                .param("status", ProjectStatus.NOT_STARTED.name())
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(
                        document("get-my-project-list",
                                requestParameters(
                                        parameterWithName("page").description("현재 페이지"),
                                        parameterWithName("perSize").description("페이지 아이템 개수"),
                                        parameterWithName("status").description("프로젝트 진행 상태")
                                ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("page").description("현재 페이지"),
                                        fieldWithPath("perSize").description("페이지 아이템 개수"),
                                        fieldWithPath("totalCount").description("총 아이템 개수"),
                                        fieldWithPath("totalPages").description("총 페이지 개수"),
                                        fieldWithPath("prev").description("이전 페이지 존재 여부"),
                                        fieldWithPath("next").description("다음 페이지 존재 여부"),
                                        fieldWithPath("contents[].id").description("프로젝트 식별자"),
                                        fieldWithPath("contents[].name").description("프로젝트명"),
                                        fieldWithPath("contents[].type").description("프로젝트 종류"),
                                        fieldWithPath("contents[].startDate").description("프로젝트 시작일"),
                                        fieldWithPath("contents[].endDate").description("프로젝트 종료일"),
                                        fieldWithPath("contents[].status").description("프로젝트 진행 상태"),
                                        fieldWithPath("contents[].memberCount").description("팀원 수"),
                                        fieldWithPath("contents[].professor").description("교수명"),
                                        fieldWithPath("contents[].department.id").description("학과 식별자"),
                                        fieldWithPath("contents[].department.collegeName").description("단과대학 이름"),
                                        fieldWithPath("contents[].department.name").description("학과 이름"),
                                        fieldWithPath("contents[].department.mainBranchType").description("주 캠퍼스 여부"),
                                        fieldWithPath("contents[].department.region").description("지역"),
                                        fieldWithPath("contents[].department.university.id").description("대학교 식별자"),
                                        fieldWithPath("contents[].department.university.name").description("대학교 이름"),
                                        fieldWithPath("contents[].department.university.emailDomain").description("대학교 이메일 도메인"),
                                        fieldWithPath("contents[].lectureTimes[].dayOfWeek").description("활동 요일"),
                                        fieldWithPath("contents[].lectureTimes[].startTime").description("시작 시간"),
                                        fieldWithPath("contents[].field").description("관심 분야"),
                                        fieldWithPath("contents[].fieldCategory").description("관심 분야 카테고리")
                                )
                        )
                );
    }

    @DisplayName("내 팀플 상세 조회")
    @Test
    @WithMockUser
    void getProjectDetails() throws Exception {
        // given
        Long projectId = 1L;
        ProjectResponse.Detail response = mock(ProjectResponse.Detail.class, RETURNS_DEEP_STUBS);
        when(response.getMemberList()).thenReturn(List.of(mock(ProjectMemberResponse.Summary.class, RETURNS_DEEP_STUBS)));
        when(response.getLectureTimes()).thenReturn(List.of(mock(LectureTimeResponse.class)));

        given(projectService.getProject(anyLong(), any(User.class))).willReturn(response);

        // then
        mockMvc.perform(
                        get("/projects/{projectId}", projectId)
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andDo(
                        document("get-my-project",
                                pathParameters(
                                        parameterWithName("projectId").description("팀플 식별자")
                                ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("id").description("프로젝트 식별자"),
                                        fieldWithPath("name").description("프로젝트명"),
                                        fieldWithPath("type").description("프로젝트 종류"),
                                        fieldWithPath("startDate").description("프로젝트 시작일"),
                                        fieldWithPath("endDate").description("프로젝트 종료일"),
                                        fieldWithPath("status").description("프로젝트 진행 상태"),
                                        fieldWithPath("reviewComplete").description("리뷰 완료 여부"),
                                        fieldWithPath("memberCount").description("팀원 수"),
                                        fieldWithPath("memberList[].memberId").description("회원 식별자"),
                                        fieldWithPath("memberList[].nickname").description("프로젝트 멤버 닉네임"),
                                        fieldWithPath("memberList[].admissionYear").description("학번"),
                                        fieldWithPath("memberList[].personality.adjective").description("성향 형용사"),
                                        fieldWithPath("memberList[].personality.noun").description("성향 명사"),
                                        fieldWithPath("memberList[].personality.tag").description("성향 형용사 + 명사 태그"),
                                        fieldWithPath("memberList[].expelled").description("내보내기 여부"),
                                        fieldWithPath("memberList[].reviewed").description("후기 작성 여부"),
                                        fieldWithPath("memberList[].me").description("본인 여부"),
                                        fieldWithPath("professor").description("교수명"),
                                        fieldWithPath("department.id").description("학과 식별자"),
                                        fieldWithPath("department.collegeName").description("단과대학 이름"),
                                        fieldWithPath("department.name").description("학과 이름"),
                                        fieldWithPath("department.mainBranchType").description("주 캠퍼스 여부"),
                                        fieldWithPath("department.region").description("지역"),
                                        fieldWithPath("department.university.id").description("대학교 식별자"),
                                        fieldWithPath("department.university.name").description("대학교 이름"),
                                        fieldWithPath("department.university.emailDomain").description("대학교 이메일 도메인"),
                                        fieldWithPath("lectureTimes[].dayOfWeek").description("활동 요일"),
                                        fieldWithPath("lectureTimes[].startTime").description("시작 시간"),
                                        fieldWithPath("field").description("관심 분야"),
                                        fieldWithPath("fieldCategory").description("관심 분야 카테고리"),
                                        fieldWithPath("createdDate").description("생성일시"),
                                        fieldWithPath("lastModifiedDate").description("수정일시"),
                                        fieldWithPath("createdBy").description("생성자"),
                                        fieldWithPath("lastModifiedBy").description("수정자")
                                )
                        )
                );
    }

    @DisplayName("프로젝트 팀원 등록")
    @Test
    @WithMockUser
    void addProjectMember() throws Exception {
        // given
        ProjectMemberRequest.Add request = ProjectTestFactory.createProjectMemberAddRequest();

        // then
        mockMvc.perform(
                        post("/projects/project-members")
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("add-project-member",
                                requestFields(
                                        fieldWithPath("applicantMemberId").description("지원자 회원 식별자"),
                                        fieldWithPath("recruitingId").description("모집글 식별자")
                                )
                        )
                );
    }

}