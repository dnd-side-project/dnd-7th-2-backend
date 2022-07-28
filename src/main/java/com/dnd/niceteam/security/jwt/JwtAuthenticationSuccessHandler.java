package com.dnd.niceteam.security.jwt;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.member.domain.Member;
import com.dnd.niceteam.member.repository.MemberRepository;
import com.dnd.niceteam.security.auth.dto.AuthResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String username = authentication.getName();
        String accessToken = jwtTokenProvider.createAccessToken(username);
        String refreshToken = jwtTokenProvider.createRefreshToken(username);
        Member member = memberRepository.findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username = " + username));
        member.setRefreshToken(refreshToken);
        AuthResponseDto.TokenInfo tokenDto = new AuthResponseDto.TokenInfo();
        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);
        ApiResult<AuthResponseDto.TokenInfo> result = ApiResult.success(tokenDto);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), result);
    }
}
