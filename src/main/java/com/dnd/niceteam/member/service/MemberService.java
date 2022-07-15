package com.dnd.niceteam.member.service;

import com.dnd.niceteam.member.domain.Member;
import com.dnd.niceteam.member.dto.MemberRequestDto;
import com.dnd.niceteam.member.dto.MemberResponseDto;
import com.dnd.niceteam.member.repository.MemberRepository;
import com.dnd.niceteam.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public MemberResponseDto.TokenInfo login(MemberRequestDto.Login loginRequestDto) {
        // 1. 존재 여부 검증
        // TODO: 2022-07-14 예외 throw
        Member member = memberRepository.findOneByUsername(loginRequestDto.getUsername())
                .orElseThrow(null);

        // 2. 인증(Authentication) 객체 및 검증
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3.jwt 생성 및 세팅
        MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateTokens(authenticate);
        // TODO: 2022-07-14 이미 로그인 되어있는지 체크 로직. 우선 존재여부 상관없이 다시 업데이트.
        member.setTokenInfo(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());

        // TODO: 2022-07-14 ApiResponseOjbect 생성 후 대체
        return tokenInfo;
    }

    public MemberResponseDto.Reissue reissueAccessToken(MemberRequestDto.Reissue reissueRequestDto) {
        // TODO: 2022-07-15 예외 생성
        Member member = memberRepository.findOneByUsername(reissueRequestDto.getUsername())
                .orElseThrow(null);

        // 1. Refresh Token 유효성 및 일치 체크
        if (!jwtTokenProvider.validateToken(reissueRequestDto.getRefreshToken())
                || !member.getRefreshToken().equals(reissueRequestDto.getRefreshToken())) {
            // TODO: 2022-07-14 Refresh Token 유효 X 예외 생성
        }

        // TODO: 2022-07-15 로그아웃된 경우 예외 처리

        // 2. AccessToken 재발급
        Authentication authentication = jwtTokenProvider.getAuthentication(member.getAccessToken());
        MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateTokens(authentication);

        // 3. accesstoken update
        member.updateAccessToken(tokenInfo.getAccessToken());

        // TODO: 2022-07-15 ApiResponseOjbect 생성 후 대체
        return new MemberResponseDto.Reissue(tokenInfo.getAccessToken());
    }
}