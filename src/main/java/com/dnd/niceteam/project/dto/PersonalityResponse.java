package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.code.Personality;
import lombok.Data;

@Data
public class PersonalityResponse {

    private Personality.Adjective adjective;
    private Personality.Noun noun;

    public String getTag() {
        return String.format("%s %s", adjective.getTitle(), noun.getTitle());
    }

    public static PersonalityResponse from(Personality personality) {
        PersonalityResponse dto = new PersonalityResponse();

        dto.setAdjective(personality.getAdjective());
        dto.setNoun(personality.getNoun());

        return dto;
    }

}
