package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.LectureTime;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface LectureProjectRequest {

    @Data
    class Register {

        private String name;

        private LocalDate startDate;

        private LocalDate endDate;

        private String professor;

        private Long departmentId;

        private List<LectureTimeRequest> lectureTimes;

        public LectureProject toEntity(Department department, Set<LectureTime> lectureTimes) {
            return LectureProject.builder()
                    .name(name)
                    .startDate(startDate)
                    .endDate(endDate)
                    .professor(professor)
                    .department(department)
                    .lectureTimes(lectureTimes)
                    .build();
        }

    }

}
