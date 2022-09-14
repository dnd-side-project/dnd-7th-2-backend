package com.dnd.niceteam.domain.code;

import com.dnd.niceteam.domain.common.EnumMapperType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteType implements EnumMapperType {

    PROJECT_COMPLETE("팀플 완료 투표")
    , EXPEL("내보내기 투표")
    ;

    private final String title;

    @Override
    public String getCode() {
        return name();
    }

    @JsonCreator
    public static VoteType fromJson(@JsonProperty("code") String code) {
        return valueOf(code);
    }

}