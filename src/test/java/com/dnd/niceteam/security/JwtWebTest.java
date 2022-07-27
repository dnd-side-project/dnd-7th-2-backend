package com.dnd.niceteam.security;

import com.dnd.niceteam.common.RestDocsConfig;
import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.error.exception.ErrorCode;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(RestDocsConfig.class)
@Transactional
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class JwtWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Test
    @DisplayName("만료된 토큰으로 요청")
    void expiredJwtToken() throws Exception {
        //given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
                0L, 3600L, jwtSecret, userDetailsService);
        Member member = memberRepository.save(Member.builder()
                .username("test-username")
                .password(passwordEncoder.encode("testPassword11!"))
                .email("test@email.com")
                .name("test-name")
                .build());
        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getUsername());
        member.setRefreshToken(refreshToken);
        em.flush();
        em.clear();

        //expected
        mockMvc.perform(get("/not-exist-path-for-test")
                        .header(HttpHeaders.AUTHORIZATION, JwtTokenProvider.TOKEN_PREFIX + accessToken))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(ApiResult.Status.FAIL.name()))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.HANDLE_UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.error.message").value(ErrorCode.HANDLE_UNAUTHORIZED.getMessage()))
                .andExpect(jsonPath("$.error.errors").isEmpty());
    }
}
