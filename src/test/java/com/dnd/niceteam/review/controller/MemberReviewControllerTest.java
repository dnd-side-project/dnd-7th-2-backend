package com.dnd.niceteam.review.controller;

import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.common.jackson.RestDocsObjectMapper;
import com.dnd.niceteam.review.MemberReviewTestFactory;
import com.dnd.niceteam.review.dto.MemberReviewRequest;
import com.dnd.niceteam.review.service.MemberReviewService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ RestDocsConfig.class, RestDocsObjectMapper.class })
@AutoConfigureRestDocs
@WebMvcTest(
        controllers = MemberReviewController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
class MemberReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestDocsObjectMapper objectMapper;

    @MockBean
    private MemberReviewService memberReviewService;

    @Test
    @WithMockUser
    @DisplayName("팀원 후기 등록")
    void addMemberReview() throws Exception {
        // given
        MemberReviewRequest.Add request = MemberReviewTestFactory.getAddRequest();

        // then
        mockMvc.perform(
                post("/member-reviews")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("add-member-review",
                            requestFields(
                                    fieldWithPath("participationScore").description("참여도 점수"),
                                    fieldWithPath("teamAgainScore").description("재팀원 희망 점수"),
                                    fieldWithPath("reviewTags[].code").description("후기 태그 코드"),
                                    fieldWithPath("reviewTags[].title").description("후기 태그"),
                                    fieldWithPath("projectId").description("프로젝트 식별자"),
                                    fieldWithPath("revieweeId").description("피평가자 아이디")
                            )
                        )
                );
    }

    @Test
    @WithMockUser
    @DisplayName("팀원 후기 건너뛰기")
    void skipMemberReview() throws Exception {
        // given
        MemberReviewRequest.Skip request = MemberReviewTestFactory.getSkipRequest();

        // then
        mockMvc.perform(
                        post("/member-reviews/skip")
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("skip-member-review",
                                requestFields(
                                        fieldWithPath("projectId").description("프로젝트 식별자"),
                                        fieldWithPath("revieweeId").description("피평가자 아이디")
                                )
                        )
                );
    }

}