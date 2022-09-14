package com.dnd.niceteam.domain.code;

import com.dnd.niceteam.domain.common.EnumMapperType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DayOfWeek implements EnumMapperType {

    MON("월"),
    TUE("화"),
    WED("수"),
    THUR("목"),
    FRI("금"),
    SAT("토"),
    SUN("일"),
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
    public static DayOfWeek fromJson(@JsonProperty("code") String code) {
        return valueOf(code);
    }

}
