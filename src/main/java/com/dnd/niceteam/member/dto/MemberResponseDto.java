package com.dnd.niceteam.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class MemberResponseDto {
    @Data
    public static class TokenInfo {
        // private String grantType
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }
    @Getter
    @AllArgsConstructor
    public static class Reissue {
        private String accessToken;
    }
}
