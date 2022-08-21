package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.domain.project.SideProject;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface ProjectResponse {

    @Data
    class ListItem {

        private Long id;

        // Project 상태
        private String name;
        private Type type;

        private LocalDate startDate;
        private LocalDate endDate;

        private ProjectStatus status;

        private Integer memberCount;

        // Lecture Project 상태
        private String professor;
        private DepartmentResponse department;
        private List<LectureTimeResponse> lectureTimes;

        // Side Project
        private Field field;
        private FieldCategory fieldCategory;

        public static ListItem of(Project project) {
            ListItem dto = new ListItem();

            dto.setId(project.getId());

            // Project 상태
            dto.setName(project.getName());
            dto.setType(project.getType());
            dto.setStatus(project.getStatus());

            dto.setStartDate(project.getStartDate());
            dto.setEndDate(project.getEndDate());

            // 계산된 값
            dto.setMemberCount(project.getMemberCount());

            if (project instanceof LectureProject)          setLectureDetails((LectureProject) project, dto);
            else                                            setSideDetails((SideProject) project, dto);

            return dto;
        }

        public static void setLectureDetails(LectureProject lecture, ListItem dto) {
            List<LectureTimeResponse> lectureTimes = lecture.getLectureTimes().stream()
                    .map(LectureTimeResponse::from).collect(Collectors.toList());

            // Lecture Project 상태
            dto.setProfessor(lecture.getProfessor());
            dto.setDepartment(DepartmentResponse.from(lecture.getDepartment()));
            dto.setLectureTimes(lectureTimes);
        }

        public static void setSideDetails(SideProject side, ListItem dto) {
            // Side Project 상태
            dto.setField(side.getField());
            dto.setFieldCategory(side.getFieldCategory());
        }

    }

    @Data
    class Detail {

        private Long id;

        // Project 상태
        private String name;
        private Type type;

        private LocalDate startDate;
        private LocalDate endDate;

        private ProjectStatus status;
        private Integer memberCount;
        private List<ProjectMemberResponse.Summary> memberList;

        // Lecture Project 상태
        private String professor;
        private DepartmentResponse department;
        private List<LectureTimeResponse> lectureTimes;

        // Side Project
        private Field field;
        private FieldCategory fieldCategory;

        // Auditing 정보
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
        private String createdBy;
        private String lastModifiedBy;

        public static Detail from(Project project) {
            return from(project, null);
        }

        public static Detail from(Project project, List<ProjectMemberResponse.Summary> memberList) {
            return project instanceof LectureProject ?
                    from((LectureProject) project, memberList) :
                    from((SideProject)    project, memberList);
        }

        public static Detail from(LectureProject lecture, List<ProjectMemberResponse.Summary> memberList) {
            Detail dto = new Detail();

            DepartmentResponse departmentResponse =     DepartmentResponse.from(lecture.getDepartment());
            List<LectureTimeResponse> lectureTimes =    lecture.getLectureTimes().stream()
                                                            .map(LectureTimeResponse::from).collect(Collectors.toList());

            setProjectCommonDetails(lecture, dto, memberList);

            // Lecture Project 상태
            dto.setProfessor(lecture.getProfessor());
            dto.setDepartment(departmentResponse);
            dto.setLectureTimes(lectureTimes);

            return dto;
        }

        public static Detail from(SideProject side, List<ProjectMemberResponse.Summary> memberList) {
            Detail dto = new Detail();

            setProjectCommonDetails(side, dto, memberList);

            // Side Project 상태
            dto.setField(side.getField());
            dto.setFieldCategory(side.getFieldCategory());

            return dto;
        }

        private static void setProjectCommonDetails(Project project, Detail dto, List<ProjectMemberResponse.Summary> memberList) {
            dto.setId(project.getId());

            // Project 상태
            dto.setName(project.getName());
            dto.setType(project.getType());
            dto.setStatus(project.getStatus());

            dto.setStartDate(project.getStartDate());
            dto.setEndDate(project.getEndDate());

            dto.setMemberCount(project.getMemberCount());

            dto.setMemberList(Objects.requireNonNullElseGet(memberList, () -> project.getProjectMembers().stream()
                    .map(ProjectMemberResponse.Summary::from).collect(Collectors.toList())));

            // Auditing 정보
            dto.setCreatedDate(project.getCreatedDate());
            dto.setLastModifiedDate(project.getLastModifiedDate());
            dto.setCreatedBy(project.getCreatedBy());
            dto.setLastModifiedBy(project.getLastModifiedBy());
        }

    }

}
