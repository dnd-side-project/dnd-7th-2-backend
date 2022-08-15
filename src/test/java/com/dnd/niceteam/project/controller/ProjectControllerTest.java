package com.dnd.niceteam.project.controller;

import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.project.dto.LectureTimeResponse;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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

    @Test
    @WithMockUser
    @DisplayName("프로젝트 목록 조회")
    void addMemberReview() throws Exception {
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

}