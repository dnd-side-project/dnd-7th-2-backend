package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.domain.project.ProjectType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ProjectResponse {

    @Data
    class Detail {

        private Long id;

        // Project 상태
        private String name;
        private ProjectType type;
        private ProjectStatus status;

        private LocalDate startDate;
        private LocalDate endDate;

        // Auditing 정보
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
        private String createdBy;
        private String lastModifiedBy;

        public static Detail from(Project project) {
            Detail self = new Detail();
            self.setId(project.getId());

            // Project 상태
            self.setName(project.getName());
            self.setType(project.getType());
            self.setStatus(project.getStatus());

            self.setStartDate(project.getStartDate());
            self.setEndDate(project.getEndDate());

            // Auditing 정보
            self.setCreatedDate(project.getCreatedDate());
            self.setLastModifiedDate(project.getLastModifiedDate());
            self.setCreatedBy(project.getCreatedBy());
            self.setLastModifiedBy(project.getLastModifiedBy());

            return self;
        }

    }

}
