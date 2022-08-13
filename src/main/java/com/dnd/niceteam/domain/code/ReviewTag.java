package com.dnd.niceteam.domain.code;

import com.dnd.niceteam.domain.common.EnumMapperType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReviewTag implements EnumMapperType {
    RESPONSIBILITY("책임감 굿"),
    DEAD_LINE("마감을 칼같이"),
    MOOD_MAKER("분위기 메이커"),
    TIME_MANNERS("시간매너 끝판왕"),
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
