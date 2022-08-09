package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.university.University;
import lombok.Data;

@Data
public class UniversityResponse {

    private Long id;

    private String name;

    private String emailDomain;

    public static UniversityResponse from(University university) {
        UniversityResponse dto = new UniversityResponse();

        dto.setId(university.getId());

        dto.setName(university.getName());
        dto.setEmailDomain(university.getEmailDomain());

        return dto;
    }
}
