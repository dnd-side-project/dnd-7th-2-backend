package com.dnd.niceteam.domain.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Personality {

    @Enumerated(EnumType.STRING)
    @Column(name = "personality_adjective", length = 15, nullable = false)
    private Adjective adjective;

    @Enumerated(EnumType.STRING)
    @Column(name = "personality_noun", length = 15, nullable = false)
    private Noun noun;

    public enum Adjective {
        LOGICAL,        // 논리적인
        PRECISE,        // 꼼꼼한
        PLANNED,        // 계획적인
        QUICK_WORKING,  // 일처리가 빠른
        CHEERFUL,       // 쾌활한
        CREATIVE,       // 창의적인
        SINCERE,        // 성실한
        GOAL_ORIENTED,  // 목표지향적인
        PERSISTENT,     // 끈기있는
    }

    public enum Noun {
        INVENTOR,               // 발명가
        LEADER,                 // 리더
        FOLLOWER,               // 팔로워
        PERFECTIONIST,          // 완벽주의자
        COMMUNICATOR,           // 커뮤니케이터
        ADVENTURER,             // 모험가
        ANALYST,                // 분석가
        MEDIATOR,               // 중재자
        JACK_OF_ALL_TRADES,   // 만능재주꾼
    }
}
