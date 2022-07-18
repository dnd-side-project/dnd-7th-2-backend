package com.dnd.niceteam.security.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class AuthResponseDto {
    @Data
    @AllArgsConstructor
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    public static class Reissue {
        private String accessToken;
    }
}
