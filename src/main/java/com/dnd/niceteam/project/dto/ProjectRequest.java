package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.LectureTime;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.domain.project.SideProject;
import com.dnd.niceteam.domain.recruiting.exception.InvalidRecruitingTypeException;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.dto.RecruitingModify;
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

        public static ProjectRequest.Register createProjecRegistertRequestDto(RecruitingCreation.RequestDto recruitingReqDto) {
            ProjectRequest.Register projectDto = new ProjectRequest.Register();
            projectDto.setType(recruitingReqDto.getRecruitingType());
            projectDto.setStartDate(recruitingReqDto.getProjectStartDate());
            projectDto.setEndDate(recruitingReqDto.getProjectEndDate());
            projectDto.setName(recruitingReqDto.getProjectName());

            switch (recruitingReqDto.getRecruitingType()) {
                case SIDE:
                    projectDto.setField(recruitingReqDto.getField());
                    projectDto.setFieldCategory(recruitingReqDto.getFieldCategory());
                    break;
                case LECTURE:
                    projectDto.setProfessor(recruitingReqDto.getProfessor());
                    projectDto.setDepartmentId(recruitingReqDto.getDepartmentId());
                    projectDto.setLectureTimes(recruitingReqDto.getLectureTimes());
                    break;
                default:
                    throw new InvalidRecruitingTypeException("Unexpected Type: " + recruitingReqDto.getRecruitingType());
            }
            return projectDto;
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

        public static ProjectRequest.Update createProjectUpdateRequestDto (RecruitingModify.RequestDto requestDto) {
            ProjectRequest.Update dto = new ProjectRequest.Update();
            dto.setName(requestDto.getProjectName());
            dto.setStartDate(requestDto.getProjectStartDate());
            dto.setEndDate(requestDto.getProjectEndDate());

            switch (requestDto.getRecruitingType()) {
                case SIDE:
                    dto.setField(requestDto.getField());
                    dto.setFieldCategory(requestDto.getFieldCategory());
                    break;
                case LECTURE:
                    dto.setProfessor(requestDto.getProfessor());
                    dto.setDepartmentId(requestDto.getDepartmentId());
                    dto.setLectureTimes(requestDto.getLectureTimes());
                    break;
                default:
                    throw new InvalidRecruitingTypeException("Unexpected Type: " + requestDto.getRecruitingType());
            }
            return dto;
        }
    }

}