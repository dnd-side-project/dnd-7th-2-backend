package com.dnd.niceteam.university.dto;

import com.dnd.niceteam.domain.university.University;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UniversityDto {

    private Long id;

    private String name;

    private String emailDomain;

    public static UniversityDto of(University university) {
        return new UniversityDto(university.getId(), university.getName(), university.getEmailDomain());
    }
}
