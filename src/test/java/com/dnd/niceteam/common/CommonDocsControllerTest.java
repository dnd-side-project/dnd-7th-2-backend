package com.dnd.niceteam.common;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({RestDocsConfig.class})
@WebMvcTest(CommonDocsController.class)
@AutoConfigureRestDocs
class CommonDocsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void apiResult() throws Exception {
        mockMvc.perform(get("/docs/common/api-result")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer authentication-token"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("common-ApiResult",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION)
                                        .description("인증이 필요한 요청의 경우 해당 헤더에 인증 토큰을 포함")
                        ),
                        responseFields(
                            fieldWithPath("status").description("API 응답 상태"),
                                fieldWithPath("data").type(Object.class).optional()
                                        .description("응답 결과 데이터\n- 응답이 실패한 경우 포함되지 않음"),
                                fieldWithPath("error").optional()
                                        .description("에러 결과\n- 에러가 없는 경우 포함되지 않음"),
                                fieldWithPath("error.code").description("에러코드 - 규칙: Domain-Num"),
                                fieldWithPath("error.message").description("에러 메세지"),
                                fieldWithPath("error.errors").optional().description(
                                        "요청 필드에 대한 예외 상황들\n- 필드 예외가 없는 경우 빈 배열 []"),
                                fieldWithPath("error.errors[].field").description("예외가 발생한 요청 필드"),
                                fieldWithPath("error.errors[].value").description("예외가 발생한 요청 값"),
                                fieldWithPath("error.errors[].reason").description("예외가 발생한 원인")
                        )
                ))
        ;
    }
}
