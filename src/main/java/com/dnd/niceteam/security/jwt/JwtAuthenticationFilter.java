package com.dnd.niceteam.security.jwt;

import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.security.auth.dto.AuthRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
            "/login", HttpMethod.POST.name());

    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider, AccountRepository accountRepository,
            AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler(
                jwtTokenProvider, accountRepository, objectMapper));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        AuthRequestDto.Login loginDto = objectMapper.readValue(request.getInputStream(), AuthRequestDto.Login.class);
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(
                loginDto.getUsername(), loginDto.getPassword());
        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
