package com.dnd.niceteam.security.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface AuthResponseDto {

    @Data
    class TokenInfo {
        private String accessToken;
        private String refreshToken;
    }

    @Data
    @AllArgsConstructor
    class Reissue {
        private String accessToken;
    }
}
