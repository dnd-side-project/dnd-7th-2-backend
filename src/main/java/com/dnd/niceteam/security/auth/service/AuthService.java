package com.dnd.niceteam.security.auth.service;

import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.member.exception.MemberNotFoundException;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.security.auth.dto.AuthRequestDto;
import com.dnd.niceteam.security.auth.dto.AuthResponseDto;
import com.dnd.niceteam.security.exception.RefreshTokenIsNullException;
import com.dnd.niceteam.security.exception.TokenInvalidException;
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
    public AuthResponseDto.TokenInfo login(AuthRequestDto.Login loginRequestDto) {
        // 1. 존재 여부 검증
        Account account = accountRepository.findByEmail(loginRequestDto.getUsername())
                .orElseThrow(()-> new MemberNotFoundException("UserDetailsServiceImpl.findSecurityUserByUsername - " +
                        "username = " + loginRequestDto.getUsername()));

        // 2. 인증(Authentication) 객체 및 검증
        // TODO: 2022-07-16 Security Filter에서 처리할 예정

        // 3.jwt 생성 및 세팅
        AuthResponseDto.TokenInfo tokenInfo = new AuthResponseDto.TokenInfo();
        tokenInfo.setAccessToken(jwtTokenProvider.createAccessToken(account.getEmail()));
        tokenInfo.setRefreshToken(jwtTokenProvider.createRefreshToken(account.getEmail()));

        // 이미 로그인 되어있어도 다시 업데이트
        account.setRefreshToken(tokenInfo.getRefreshToken());

        return tokenInfo;
    }

    @Transactional
    public AuthResponseDto.Reissue reissueAccessToken(AuthRequestDto.Reissue reissueRequestDto) {
        Account account = accountRepository.findByEmail(reissueRequestDto.getUsername())
                .orElseThrow(()-> new MemberNotFoundException("UserDetailsServiceImpl.findSecurityUserByUsername " +
                        "- username = " + reissueRequestDto.getUsername()));

        // 로그아웃한 상태인 경우, 사용자가 재 로그인하도록 응답
        if (account.getRefreshToken() == null) {
            throw new RefreshTokenIsNullException("AuthService.reissueAccessToken - 로그아웃 상태입니다. " +
                    "username = " + account.getEmail());
        }

        // 토큰이 유효하지 않거나 일치하지 않는 토큰을 보낸 경우, 재 로그인하도록 응답
        // TODO: 2022-07-16 Filter 내에서 검증 예정 => 구현 진행되지 않아서 우선 service 메서드내에서 처리
        if (!jwtTokenProvider.validateToken(reissueRequestDto.getRefreshToken())
                || !account.getRefreshToken().equals(reissueRequestDto.getRefreshToken())) {
            throw new TokenInvalidException("AuthService.reissueAccessToken - 유효하지 않는 토큰입니다.");
        }

        // AccessToken 재발급
        AuthResponseDto.TokenInfo tokenInfo = new AuthResponseDto.TokenInfo();
        tokenInfo.setAccessToken(jwtTokenProvider.createAccessToken(account.getEmail()));
        tokenInfo.setAccessToken(jwtTokenProvider.createRefreshToken(account.getEmail()));

        return new AuthResponseDto.Reissue(tokenInfo.getAccessToken());
    }

    @Transactional
    public void logout(String username) {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(()-> new MemberNotFoundException(
                        "UserDetailsServiceImpl.findSecurityUserByUsername - username = " + username));

        account.setRefreshToken(null);
    }
}
