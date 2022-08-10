package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.dnd.niceteam.domain.project.SideProject;
import lombok.Data;

import java.time.LocalDate;

public interface SideProjectRequest {

    @Data
    class Register {

        private String name;

        private LocalDate startDate;

        private LocalDate endDate;

        private FieldCategory fieldCategory;

        private Field field;

        public SideProject toEntity() {
            return SideProject.builder()
                    .name(name)
                    .startDate(startDate)
                    .endDate(endDate)
                    .field(field)
                    .fieldCategory(fieldCategory)
                    .build();
        }

    }

}
