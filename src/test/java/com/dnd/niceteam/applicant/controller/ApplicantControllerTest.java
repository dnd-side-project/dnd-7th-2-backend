package com.dnd.niceteam.applicant.controller;

import com.dnd.niceteam.applicant.dto.ApplicantCreation;
import com.dnd.niceteam.applicant.dto.ApplicantFind;
import com.dnd.niceteam.applicant.service.ApplicantService;
import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(controllers = ApplicantController.class)
class ApplicantControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ApplicantService applicantService;

    private static final Long RECRUITING_ID = 1L;

    @Test
    @WithMockUser
    @DisplayName("모집 지원 등록 API")
    public void applicantCreate() throws Exception {
        // given
        ApplicantCreation.ResponseDto responseDto = new ApplicantCreation.ResponseDto();
        responseDto.setId(1L);
        given(applicantService.addApplicant(anyLong(), anyString())).willReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(
                post("/recruiting/{recruitingId}/applicant", RECRUITING_ID)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
        );
        // then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("applicant-create",
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("id").description("지원자 식별자(ID)")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    @DisplayName("모집 지원 취소 API")
    public void applicantDelete() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                delete("/recruiting/{recruitingId}/applicant", RECRUITING_ID)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
        );
        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("applicant-delete"
                        )
                );
    }

    @Test
    @WithMockUser
    @DisplayName("모집글 지원 현황 목록 조회 API")
    public void applicantList() throws Exception {
        // given
        ApplicantFind.ListRequestDto request = new ApplicantFind.ListRequestDto();
        request.setApplicantJoined(Boolean.TRUE);
        request.setRecruitingStatus(RecruitingStatus.IN_PROGRESS);
        ApplicantFind.ListResponseDto responseDto = new ApplicantFind.ListResponseDto();
        responseDto.setRecruitingId(1L);
        responseDto.setRecruitingStatus(RecruitingStatus.IN_PROGRESS);
        responseDto.setJoined(Boolean.FALSE);
        responseDto.setRecruitingMemberCount(3);
        responseDto.setProjectName("project-name");

        Pagination<ApplicantFind.ListResponseDto> applicantResponsePage = Pagination.<ApplicantFind.ListResponseDto>builder()
                .page(0)
                .perSize(10)
                .totalCount(1)
                .contents(List.of(responseDto))
                .build();
        when(applicantService.getMyApplicnts(anyInt(), anyInt(), eq(request), anyString()))
                .thenReturn(applicantResponsePage);

        // when
        ResultActions result = mockMvc.perform(
                get("/recruiting/applicant/me")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("page", "1")
                        .param("perSize", "10")
        );
        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("my-applicant-list",
                        requestParameters(
                                parameterWithName("page").description("현재 페이지(입력하지 않을 경우, 1)").optional(),
                                parameterWithName("perSize").description("페이지 별 아이템 개수(입력하지 않을 경우, 10)").optional()
                        ),
                        requestFields(
                                fieldWithPath("recruitingStatus").description("모집글 상태").description("필터링에 따른 짝을 알려줘야함..").optional(),
                                fieldWithPath("applicantJoined").description("지원 수락 여부").description("필터링에 따른 짝을 알려줘야함..").optional()
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
                                fieldWithPath("contents[].recruitingStatus").description("모집글 상태"),
                                fieldWithPath("contents[].joined").description("지원 수락 여부"),
                                fieldWithPath("contents[].projectName").description("프로젝트명"),
                                fieldWithPath("contents[].recruitingMemberCount").description("모집 인원")
                        )
                ));
    }
}