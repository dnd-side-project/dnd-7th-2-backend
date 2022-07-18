package com.dnd.niceteam.security.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class AuthResponseDto {

    @Data
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
    }

    @Data
    @AllArgsConstructor
    public static class Reissue {
        private String accessToken;
    }
}
