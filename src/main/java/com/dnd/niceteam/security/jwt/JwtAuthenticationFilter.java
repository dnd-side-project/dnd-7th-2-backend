package com.dnd.niceteam.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author      : 문 윤지
 * Description : 요청의 header 내에 jwt 토큰 유효성 검증 필터 (토큰 유효하면 SecurityContextHolder에 set)
 * History     : [2022-07-15] 문윤지 - Class Create
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token= request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            token = token.substring(TOKEN_PREFIX.length());

            if (jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // TODO: 2022-07-14 만료기한이 지난 토큰인 경우, 여기서 Exception or Layer 메서드에서?
            }
        }
        filterChain.doFilter(request, response);
    }
}
