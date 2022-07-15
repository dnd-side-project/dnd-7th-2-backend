package com.dnd.niceteam.member;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class MemberService {
    private final MemberRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MamberResponseDto.TokenInfo login(UserRequestDto.Login loginRequestDto) {
        // 1. 존재 여부 검증
        // 어짜피 나중에 써야하니까 객체로 찾자.
        // TODO: 2022-07-14 예외 생성
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(null);
//            return response.fail("존재하지 않는 회원입니다.", {상태코드});

        // 2. 인증(Authentication) 객체 및 검증 진행
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        // 인증 객체로 검증 진행 (CustomUserDetailsService의 메서드로 존재 여부 체크)
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);// 어떤 token 종류냐에 따라

        System.out.println("로그- 토큰 생성 전 authentication 체크: credentials: " + authenticate.getCredentials() + ", principal(pw): " + authenticate.getPrincipal() + ", authority: " + authenticate.getAuthorities());
        // 인증 객체 기반으로 jwt 토큰 생성
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authenticate);

        // refresh token 세팅
        // TODO: 2022-07-14 이미 로그인 되어있는지 체크해야하나? 우선 존재여부 상관없이 다시 업데이트하도록 진행.
        // 이미 로그인 되어있는지 체크하려면, RefreshToken으로 db 조회하면 될듯.

        user.setTokenInfo(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());

        // TODO: 2022-07-13 ApiResponseOjbect 생성
        return tokenInfo;
    }
}
