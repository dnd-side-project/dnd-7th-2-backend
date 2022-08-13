package com.dnd.niceteam.domain.code;

import com.dnd.niceteam.domain.common.EnumMapperType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FieldCategory implements EnumMapperType {
    CONTEST("공모전"),
    EXTRA_ACTIVITY("대외활동"),
    STUDY("스터디"),
    CLUB("동아리"),
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
}
