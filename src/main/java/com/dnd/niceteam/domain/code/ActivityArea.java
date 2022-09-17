package com.dnd.niceteam.domain.code;

import com.dnd.niceteam.domain.common.EnumMapperType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityArea implements EnumMapperType {
    ONLINE("온라인"),
    SEOUL("서울"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    INCHEON("인천"),
    BUSAN("부산"),
    DAEGU("대구"),
    ULSAN("울산"),
    CHUNGCHEONGBUKDO("충청북도"),
    CHUNGCHEONGNAMDO("충청남도"),
    GYEONGSANGBUKDO("경상북도"),
    JEOLLABUKDO("전라북도"),
    JEOLLANAMDO("전라남도"),
    GYEONGSANGNAMDO("경상남도"),
    GWANGJU("광주"),
    JEJUDO("제주도")
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
    public static ActivityArea fromJson(@JsonProperty("code") String code) {
        return valueOf(code);
    }

}
