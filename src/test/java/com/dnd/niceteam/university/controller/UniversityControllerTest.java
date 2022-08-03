package com.dnd.niceteam.university.controller;

import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.security.SecurityConfig;
import com.dnd.niceteam.university.dto.DepartmentDto;
import com.dnd.niceteam.university.dto.UniversityDto;
import com.dnd.niceteam.university.service.UniversityService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(controllers = UniversityController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
class UniversityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UniversityService mockUniversityService;

    @Test
    @WithMockUser
    @DisplayName("대학교 검색 API")
    void universitySearch() throws Exception {
        // given
        given(mockUniversityService.getUniversityList("테스트")).willReturn(List.of(
                new UniversityDto(1L, "테스트1대학교", "test1.com"),
                new UniversityDto(2L, "테스트2대학교", "test2.com"),
                new UniversityDto(3L, "테스트3대학교", "test3.com")
        ));

        // expected
        mockMvc.perform(get("/universities")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("name", "테스트"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("university-search",
                        requestParameters(
                                parameterWithName("name").description("대학교 이름 검색 키워드")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").description("대학교 ID"),
                                fieldWithPath("name").description("대학교 이름"),
                                fieldWithPath("emailDomain").description("대학교 이메일 도메인")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("대학교 학과 검색 API")
    void departmentListOfUniversity() throws Exception {
        // given
        given(mockUniversityService.getDepartmentsOfUniversity(1L)).willReturn(List.of(
                new DepartmentDto(1L, "테스트단과대학", "테스트학과1"),
                new DepartmentDto(2L, "테스트단과대학", "테스트학과2"),
                new DepartmentDto(3L, "테스트단과대학", "테스트학과3")
        ));

        // expected
        mockMvc.perform(get("/universities/{universityId}/departments", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("university-department-list",
                        pathParameters(
                                parameterWithName("universityId").description("학과를 조회하려는 대학교 ID")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").description("학과 ID"),
                                fieldWithPath("collegeName").description("단과대학 이름"),
                                fieldWithPath("name").description("학과 이름")
                        )
                ));
    }
}