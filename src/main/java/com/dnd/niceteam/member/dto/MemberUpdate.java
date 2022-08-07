package com.dnd.niceteam.member.dto;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.Personality;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

public interface MemberUpdate {

    @Data
    class RequestDto {

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
        @Size(max = 300)
        private String introduction;

        @NotNull
        @URL
        @Size(max = 255)
        private String introductionUrl;
    }

    @Data
    class ResponseDto {

        private Long id;
    }
}
