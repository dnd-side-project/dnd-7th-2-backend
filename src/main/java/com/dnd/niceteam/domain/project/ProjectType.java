package com.dnd.niceteam.domain.project;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectType {

    MY_LECTURE("내 강의")
    , SIDE("사이드")
    ;

    private final String kor;

}
