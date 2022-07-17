package com.dnd.niceteam.security.jwt;

import com.dnd.niceteam.security.UserDetailsServiceImpl;
import com.dnd.niceteam.security.auth.dto.AuthResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author      : 문 윤지
 * Description : Jwt 생성 및 인증 정보 추출
 * History     : [2022-07-15] 문윤지 - Class Create
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt.access-token-validity}")
    private long accessTokenExpiredTimeInMilliseconds;
    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenExpiredTimeInMilliseconds;
    @Value("${jwt.secret}")
    private String secret;
    private Key key;
    private static final String AUTHORITIES_KEY = "auth";
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 토큰 생성
    public AuthResponseDto.TokenInfo generateTokens(String username, String role) {

        String accessToken = createAccessToken(username, role);
        String refreshToken = createRefreshToken();

        return new AuthResponseDto.TokenInfo(accessToken,refreshToken);
    }

    private String createAccessToken(String username, String role) {
            Date accessTokenExpiryTime = new Date(System.currentTimeMillis() + accessTokenExpiredTimeInMilliseconds);
        // 권한
        String authorities = convertRoleToAuthorities(role);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiryTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken() {
        Date refreshTokenExpiryTime = new Date(System.currentTimeMillis() + refreshTokenExpiredTimeInMilliseconds);
        return Jwts.builder()
                .setExpiration(refreshTokenExpiryTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 및 만료기간 검사
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = parseClaimsJws(token);
            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (io.jsonwebtoken.security.SecurityException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return false;
    }

    // 토큰에서 인증 정보 추출
    public Authentication getAuthentication(String accessToken) {
        String usernameFromToken = getUsernameFromToken(accessToken);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(usernameFromToken);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsernameFromToken(String token) {
        return parseClaimsJws(token).getBody().getSubject();
    }
    private Jws<Claims> parseClaimsJws(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    private String convertRoleToAuthorities(String fullName) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(fullName));

        return grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
