package com.dnd.niceteam.security.jwt;

import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private final AccountRepository accountRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = resolveToken(request);
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getSubject(token);
            Account account = accountRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("username = " + username));
            account.setRefreshToken(null);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtTokenProvider.TOKEN_PREFIX.length());
        }
        return null;
    }
}
