package com.dnd.niceteam.security.auth.dto;

import lombok.Data;

public interface AuthResponseDto {

    @Data
    class TokenInfo {
        private String accessToken;
        private String refreshToken;
    }
}
