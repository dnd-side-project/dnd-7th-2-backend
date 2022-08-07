package com.dnd.niceteam.domain.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityArea {
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

    private final String kor;
}
