package com.dnd.niceteam.security.auth.service;

import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.security.auth.dto.AuthRequestDto;
import com.dnd.niceteam.security.auth.dto.AuthResponseDto;
import com.dnd.niceteam.security.exception.RefreshTokenIsNullException;
import com.dnd.niceteam.security.exception.TokenInvalidException;
import com.dnd.niceteam.security.exception.UsernameNotFoundException;
import com.dnd.niceteam.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AccountRepository accountRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponseDto.TokenInfo reissueAccessToken(AuthRequestDto.Reissue reissueRequestDto) {
        Account account = accountRepository.findByEmail(reissueRequestDto.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("username = " + reissueRequestDto.getUsername()));

        // 로그아웃한 상태인 경우, 사용자가 재 로그인하도록 응답
        if (account.getRefreshToken() == null) {
            throw new RefreshTokenIsNullException("username = " + account.getEmail());
        }

        // 토큰이 유효하지 않거나 일치하지 않는 토큰을 보낸 경우, 재 로그인하도록 응답
        if (isInvalidRefreshToken(reissueRequestDto.getRefreshToken(), account)) {
            throw new TokenInvalidException("Refresh token = " + reissueRequestDto.getRefreshToken());
        }

        // AccessToken 재발급
        AuthResponseDto.TokenInfo tokenInfo = new AuthResponseDto.TokenInfo();
        String accessToken = jwtTokenProvider.createAccessToken(account.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(account.getEmail());
        tokenInfo.setAccessToken(accessToken);
        tokenInfo.setRefreshToken(refreshToken);
        account.setRefreshToken(refreshToken);

        return tokenInfo;
    }

    private boolean isInvalidRefreshToken(String refreshToken, Account account) {
        return !jwtTokenProvider.validateToken(refreshToken)
                || !jwtTokenProvider.getSubject(refreshToken).equals(account.getEmail())
                || !account.getRefreshToken().equals(refreshToken);
    }
}
