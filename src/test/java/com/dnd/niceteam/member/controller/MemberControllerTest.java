package com.dnd.niceteam.member.controller;

import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.member.dto.DupCheck;
import com.dnd.niceteam.member.service.MemberService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
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
@WebMvcTest(controllers = MemberController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    @WithMockUser
    @DisplayName("이메일 중복 확인 API")
    void memberEmailDupCheck() throws Exception {
        // given
        DupCheck.ResponseDto responseDto = new DupCheck.ResponseDto();
        responseDto.setDuplicated(false);
        given(memberService.checkEmailDuplicate("test@email.com")).willReturn(responseDto);

        // expected
        mockMvc.perform(get("/members/dup-check/email")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "test@email.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.duplicated").value(false))
                .andDo(document("member-email-dup-check",
                        requestParameters(
                                parameterWithName("email").description("중복 확인 하려는 이메일")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("duplicated").description("중복 여부. 중복이 있을 경우 true.")
                        )
                ));
    }
}