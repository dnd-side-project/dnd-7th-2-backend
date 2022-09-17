package com.dnd.niceteam.domain.code;

import com.dnd.niceteam.domain.common.EnumMapperType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReviewTag implements EnumMapperType {
    RESPONSIBILITY("책임감 굿"),
    PUNCTUALITY("마감을 칼같이"),
    MOOD_MAKER("분위기 메이커"),
    TIME_MANNERS("시간매너 끝판왕"),
    POSITIVE("긍정 태도왕"),
    IDEA("아이디어 요정"),
    FEEDBACK("활발한 피드백"),
    DECISIVE("속전속결 결정왕"),
    LIKE_MINE("남의 일도 내 일같이"),
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
    public static ReviewTag fromJson(@JsonProperty("code") String code) {
        return valueOf(code);
    }
}
