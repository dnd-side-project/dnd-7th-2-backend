package com.dnd.niceteam.member.dto;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.Personality;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Set;

public interface MemberCreation {

    @Data
    class RequestDto {

        @Email
        @Size(max = 65)
        private String email;

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$",
                message = "비밀번호는 8~20자 영문 대/소문자, 숫자를 사용하세요.")
        private String password;

        @Size(min = 1, max = 10)
        @Pattern(regexp = "^[가-힣]+", message = "공백 없이 한글만 사용하세요.")
        private String nickname;

        @NotNull
        private Personality.Adjective personalityAdjective;

        @NotNull
        private Personality.Noun personalityNoun;

        @NotNull
        @Size(max = 3)
        private Set<Field> interestingFields;

        @NotNull
        private Long departmentId;

        @Max(2023)
        @Min(2000)
        @NotNull
        private Integer admissionYear;

        @NotNull
        @Size(max = 300)
        private String introduction;

        @NotNull
        @Size(max = 255)
        private String introductionUrl;
    }

    @Data
    class ResponseDto {

        private Long id;

        private String email;
    }
}
