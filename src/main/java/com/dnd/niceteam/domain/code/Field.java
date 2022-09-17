package com.dnd.niceteam.domain.code;

import com.dnd.niceteam.domain.common.EnumMapperType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Field implements EnumMapperType {

    AD_MARKETING("광고/마케팅"),
    PLANNING_IDEA("기획/아이디어"),
    DESIGN("디자인"),
    IT_SW_GAME("IT/소프트웨어/게임"),
    MEDIA("미디어"),
    ART_MUSIC_PHYSICAL("예체능"),
    LANGUAGE("어학"),
    FINANCE_ACCOUNTING("금융/회계"),
    ETC("기타"),
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
    public static Field fromJson(@JsonProperty("code") String code) {
        return valueOf(code);
    }
}
