package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.common.EnumMapperType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitingStatus implements EnumMapperType {
    IN_PROGRESS("진행중"),
    DONE("완료"),
    FAILED("실패")
    ;
    private final String title;
    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @JsonCreator
    public static RecruitingStatus fromJson(@JsonProperty("code") String code) {
        return valueOf(code);
    }
}
