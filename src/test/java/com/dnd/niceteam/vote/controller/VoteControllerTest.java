package com.dnd.niceteam.vote.controller;

import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.common.jackson.RestDocsObjectMapper;
import com.dnd.niceteam.security.SecurityConfig;
import com.dnd.niceteam.vote.VoteTestFactory;
import com.dnd.niceteam.vote.dto.VoteRequest;
import com.dnd.niceteam.vote.service.VoteService;
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
        controllers = VoteController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
class VoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RestDocsObjectMapper objectMapper;

    @MockBean
    VoteService voteService;

    @Test
    @WithMockUser
    @DisplayName("투표 표 등록")
    void addMemberReview() throws Exception {
        // given
        VoteRequest.Add request = VoteTestFactory.createVoteToCompleteAddRequest();

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
                                        fieldWithPath("type.code").description("투표 종류 코드"),
                                        fieldWithPath("type.title").description("투표 종류"),
                                        fieldWithPath("projectId").description("프로젝트 식별자"),
                                        fieldWithPath("candidateMemberId").description("내보내기 투표 대상 멤버 식별자"),
                                        fieldWithPath("choice").description("찬성, 반대 여부")
                                )
                        )
                );
    }

}