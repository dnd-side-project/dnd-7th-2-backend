package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.domain.project.SideProject;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ProjectResponse {

    @Data
    class Detail {

        private Long id;

        // Project 상태
        private String name;
        private ProjectStatus status;

        private LocalDate startDate;
        private LocalDate endDate;

        // Lecture Project 상태
        private String professor;
        private DepartmentResponse department;

        // Side Project
        private Field field;
        private FieldCategory fieldCategory;

        // Auditing 정보
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
        private String createdBy;
        private String lastModifiedBy;

        public static ProjectResponse.Detail from(LectureProject lecture) {
            ProjectResponse.Detail dto = setProjectCommonDetails(lecture);

            // Lecture Project 상태
            dto.setProfessor(lecture.getProfessor());
            dto.setDepartment(DepartmentResponse.from(lecture.getDepartment()));

            return dto;
        }

        public static ProjectResponse.Detail from(SideProject side) {
            ProjectResponse.Detail dto = setProjectCommonDetails(side);

            // Side Project 상태
            dto.setField(side.getField());
            dto.setFieldCategory(side.getFieldCategory());

            return dto;
        }

        private static ProjectResponse.Detail setProjectCommonDetails(Project project) {
            ProjectResponse.Detail dto = new ProjectResponse.Detail();
            dto.setId(project.getId());

            // Project 상태
            dto.setName(project.getName());
            dto.setStatus(project.getStatus());

            dto.setStartDate(project.getStartDate());
            dto.setEndDate(project.getEndDate());

            // Auditing 정보
            dto.setCreatedDate(project.getCreatedDate());
            dto.setLastModifiedDate(project.getLastModifiedDate());
            dto.setCreatedBy(project.getCreatedBy());
            dto.setLastModifiedBy(project.getLastModifiedBy());

            return dto;
        }

    }

}
