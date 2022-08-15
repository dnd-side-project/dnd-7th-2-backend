package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.LectureTime;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.domain.project.SideProject;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface ProjectRequest {

    @Data
    class Register {

        private String name;

        private Type type;

        private LocalDate startDate;

        private LocalDate endDate;

        // Lecture Project
        private String professor;
        private Long departmentId;
        private List<LectureTimeRequest> lectureTimes;

        // Side Project
        private Field field;
        private FieldCategory fieldCategory;

        public LectureProject toLectureProject (Department department){
            Set<LectureTime> lectureTimes = this.lectureTimes.stream()
                    .map(LectureTimeRequest::toEntity).collect(Collectors.toSet());

            return LectureProject.builder()
                    .name(name)
                    .startDate(startDate)
                    .endDate(endDate)
                    .professor(professor)
                    .department(department)
                    .lectureTimes(lectureTimes)
                    .build();
        }

        public SideProject toSideProject () {
            return SideProject.builder()
                    .name(name)
                    .startDate(startDate)
                    .endDate(endDate)
                    .field(field)
                    .fieldCategory(fieldCategory)
                    .build();
        }
    }


    @Data
    class Update {

        private String name;
        private LocalDate startDate;
        private LocalDate endDate;
        private ProjectStatus status;

        // Lecture Project
        private String professor;
        private Long departmentId;
        private List<LectureTimeRequest> lectureTimes;

        // Side Project
        private Field field;
        private FieldCategory fieldCategory;

    }

}