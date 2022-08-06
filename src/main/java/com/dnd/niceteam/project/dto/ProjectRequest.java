package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectType;
import lombok.Data;

import java.time.LocalDateTime;

public interface ProjectRequest {

    @Data
    class Register {

        private String name;

        private ProjectType type;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        public Project toEntity() {
            return Project.builder()
                    .name(name)
                    .type(type)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
        }

    }

}
