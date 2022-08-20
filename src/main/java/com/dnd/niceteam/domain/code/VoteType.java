package com.dnd.niceteam.domain.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteType {

    PROJECT_COMPLETE("팀플 완료 투표")
    , EXPEL("내보내기 투표")
    ;

    private final String title;

}