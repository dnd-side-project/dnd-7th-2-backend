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

    }

    public enum Noun {

    }
}
