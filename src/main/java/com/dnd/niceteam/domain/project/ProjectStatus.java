package com.dnd.niceteam.domain.project;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectStatus {

    NOT_STARTED("시작 전")
    , IN_PROGRESS("진행 중")
    , DONE("완료")
    ;

    private final String kor;

}
