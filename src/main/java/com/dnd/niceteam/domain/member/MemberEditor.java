package com.dnd.niceteam.domain.member;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.Personality;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class MemberEditor {

    private String nickname;

    private Personality.Adjective personalityAdjective;

    private Personality.Noun personalityNoun;

    private Set<Field> interestingFields;

    private String introduction;

    private String introductionUrl;
}
