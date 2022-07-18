package com.dnd.niceteam.security.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AuthRequestDto {
    @Data
    public static class Login {

        @NotEmpty(message = "아이디는 필수 입력값입니다.")
        @Size(max = 45)
        private String username;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;
    }

    @Data
    public static class Reissue {
        @NotEmpty(message = "아이디는 필수 입력값입니다.")
        private String username;

        @NotEmpty(message = "refreshToken은 필수 입력값입니다.")
        private String refreshToken;
    }
}
