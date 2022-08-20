package com.dnd.niceteam.vote.controller;

import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.review.MemberReviewTestFactory;
import com.dnd.niceteam.review.dto.MemberReviewRequest;
import com.dnd.niceteam.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(
        controllers = VoteController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("투표 표 등록")
    void addMemberReview() throws Exception {
        // given
        MemberReviewRequest.Add request = MemberReviewTestFactory.getAddRequest();

        // then
        mockMvc.perform(
                        post("/votes")
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document("vote",
                                requestFields(
                                        fieldWithPath("type").description("투표 종류 (PROJECT_COMPLETE, EXPEL)"),
                                        fieldWithPath("projectId").description("프로젝트 식별자"),
                                        fieldWithPath("candidateMemberId").description("내보내기 투표 대상 멤버 식별자"),
                                        fieldWithPath("choice").description("찬성, 반대 여부")
                                )
                        )
                );
    }

}