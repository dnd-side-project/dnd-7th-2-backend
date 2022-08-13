package com.dnd.niceteam.code.controller;

import com.dnd.niceteam.code.config.EnumMapperConfig;
import com.dnd.niceteam.code.config.EnumMapperFactory;
import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.domain.common.EnumMapperType;
import com.dnd.niceteam.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({RestDocsConfig.class, EnumMapperConfig.class})
@AutoConfigureRestDocs
@WebMvcTest(controllers = CodeController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
class CodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnumMapperFactory enumMapperFactory;

    @Test
    @WithMockUser
    @DisplayName("코드 리스트 조회 API")
    void codeApi() throws Exception {
        // given
        enumMapperFactory.put(CodeEnum.class.getSimpleName(), CodeEnum.class);

        // expected
        mockMvc.perform(get("/code")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("codeTypes", "CodeEnum"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("code-api",
                        requestParameters(
                            parameterWithName("codeTypes").description("조회하려는 코드 타입들(리스트)")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("CodeEnum").description("코드 타입"),
                                fieldWithPath("CodeEnum[].code").description("코드 -> 이를 통해 요청"),
                                fieldWithPath("CodeEnum[].title").description("코드 제목")
                        )
                ));
    }

    /**
     * 코드 (EnumMapper) 가 추가될 경우 테스트에 추가
     */
    @Test
    @WithMockUser
    @DisplayName("코드 리스트 전체 조회 API")
    void codeApi_GetAll() throws Exception {
        // given

        // expected
        mockMvc.perform(get("/code")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("code-api-get-all",
                        responseFields(
                                beneathPath("data").withSubsectionId("data")
                        )
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("코드 리스트 조회 API - 존재하지 않는 코드 타입")
    void codeApi_CodeTypeNotExists() throws Exception {
        // given

        // expected
        mockMvc.perform(get("/code")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("codeTypes", "NotFoundCode1", "NotFoundCode2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.NotFoundCode1").isEmpty())
                .andExpect(jsonPath("$.data.NotFoundCode2").isEmpty())
                .andDo(document("code-api-not-found"));
    }

    @RequiredArgsConstructor
    enum CodeEnum implements EnumMapperType {
        CODE("코드 이름")
        ;

        private final String title;

        @Override
        public String getCode() {
            return name();
        }

        @Override
        public String getTitle() {
            return title;
        }
    }
}