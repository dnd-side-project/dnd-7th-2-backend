package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.ProjectStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface LectureProjectResponse {

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

        // Auditing 정보
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
        private String createdBy;
        private String lastModifiedBy;

        public static Detail from(LectureProject lecture) {
            Detail dto = new Detail();
            dto.setId(lecture.getId());

            // Project 상태
            dto.setName(lecture.getName());
            dto.setStatus(lecture.getStatus());

            dto.setStartDate(lecture.getStartDate());
            dto.setEndDate(lecture.getEndDate());

            // Lecture Project 상태
            dto.setProfessor(lecture.getProfessor());
            dto.setDepartment(DepartmentResponse.from(lecture.getDepartment()));

            // Auditing 정보
            dto.setCreatedDate(lecture.getCreatedDate());
            dto.setLastModifiedDate(lecture.getLastModifiedDate());
            dto.setCreatedBy(lecture.getCreatedBy());
            dto.setLastModifiedBy(lecture.getLastModifiedBy());

            return dto;
        }

    }

}
