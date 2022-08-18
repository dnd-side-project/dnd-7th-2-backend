package com.dnd.niceteam.recruiting.dto;

import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.SideProject;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.exception.InvalidRecruitingTypeException;
import com.dnd.niceteam.project.dto.ProjectResponse;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface RecruitingFind {
    @Data
    class DetailResponseDto {  // 상세 조회
        private String memberNickname;
        private String title;
        private String content;
        private Type recruitingType;
        private ProgressStatus recruitingStatus;
        private Integer commentCount;
        private Integer bookmarkCount;
        private String introLink;

        private Integer recruitingMemberCount;
        private ActivityArea activityArea;
        private Set<Personality.Adjective> personalityAdjectives;
        private Set<Personality.Noun> personalityNouns;
        private LocalDate RecruitingEndDate;    // 기간이 정해져있지 않으면 null

        private ProjectResponse.Detail projectResponse;

        private LocalDateTime recruitingCreatedDate;

        public static RecruitingFind.DetailResponseDto from(Recruiting recruiting) {
            RecruitingFind.DetailResponseDto dto = new RecruitingFind.DetailResponseDto();
            dto.setTitle(recruiting.getTitle());
            dto.setMemberNickname(recruiting.getMember().getNickname());
            dto.setContent(recruiting.getContent());
            dto.setRecruitingType(recruiting.getRecruitingType());
            dto.setRecruitingStatus(recruiting.getStatus());
            dto.setIntroLink(recruiting.getIntroLink());
            dto.setRecruitingMemberCount(recruiting.getRecruitingMemberCount());
            dto.setBookmarkCount(recruiting.getBookmarkCount());
            dto.setCommentCount(recruiting.getCommentCount());
            dto.setActivityArea(recruiting.getActivityArea());
            dto.setPersonalityAdjectives(recruiting.getPersonalityAdjectives());
            dto.setRecruitingEndDate(recruiting.getRecruitingEndDate());
            dto.setPersonalityNouns(recruiting.getPersonalityNouns());

            dto.setRecruitingCreatedDate(recruiting.getCreatedDate());

            // Project
            ProjectResponse.Detail projectDto;
            // TODO: 2022-08-16 DTO 구분을 위한 메서드 (리팩터링)?
            switch (recruiting.getProject().getType()) {
                case LECTURE: projectDto = ProjectResponse.Detail.from((LectureProject) recruiting.getProject()); break;
                case SIDE: projectDto = ProjectResponse.Detail.from((SideProject) recruiting.getProject()); break;
                default: throw new InvalidRecruitingTypeException("Invalid Type: " + recruiting.getProject().getType());
            }
            dto.setProjectResponse(projectDto);

            return dto;
        }
    }

    @Data
    class ListResponseDto {    // 목록 조회
        private Long id;
        private String title;
        private String content;
        private Type recruitingType;
        private ProgressStatus recruitingStatus;
        private Integer commentCount;
        private Integer bookmarkCount;
        private String projectName;

        private String professor;

        private Field field;
        private FieldCategory fieldCategory;

        public static ListResponseDto from(Recruiting recruiting) {
            ListResponseDto dto = new ListResponseDto();
            dto.setId(recruiting.getId());
            dto.setTitle(recruiting.getTitle());
            dto.setContent(recruiting.getContent());
            dto.setRecruitingType(recruiting.getRecruitingType());
            dto.setRecruitingStatus(recruiting.getStatus());
            dto.setCommentCount(recruiting.getCommentCount());
            dto.setBookmarkCount(recruiting.getBookmarkCount());
            dto.setProjectName(recruiting.getProject().getName());

            // TODO: 2022-08-16 Factory 클래스에 구분 메서드 (리팩토링)?
            switch (recruiting.getProject().getType()) {
                case SIDE:
                    SideProject sideProject = (SideProject) recruiting.getProject();
                    dto.setField(sideProject.getField());
                    dto.setFieldCategory(sideProject.getFieldCategory());
                    break;
                case LECTURE:
                    LectureProject lectureProject = (LectureProject) recruiting.getProject();
                    dto.setProfessor(lectureProject.getProfessor());
                    break;
                default:
                    throw new InvalidRecruitingTypeException("Invalid Type: " + recruiting.getProject().getType());
            }
            return dto;
        }
    }
    @Data
    class RecommendedListResponseDto {    // 추천 사이드 목록 조회
        private Long recruitingId;
        private String title;
        private Integer recruitingMemberCount;
        private Field field;
        private FieldCategory fieldCategory;
        private LocalDate projectStartDate;
        private LocalDate projectEndDate;
        private LocalDate recruitingEndDate;

        public static RecommendedListResponseDto fromRecommendedRecruiting(Recruiting recruiting) {
            RecommendedListResponseDto dto = new RecommendedListResponseDto();
            dto.setRecruitingId(recruiting.getId());
            dto.setTitle(recruiting.getTitle());
            dto.setRecruitingEndDate(recruiting.getRecruitingEndDate());
            dto.setRecruitingMemberCount(recruiting.getRecruitingMemberCount());

            SideProject project = (SideProject) recruiting.getProject();
            dto.setProjectStartDate(project.getStartDate());
            dto.setProjectEndDate(project.getEndDate());
            dto.setField(project.getField());
            dto.setFieldCategory(project.getFieldCategory());
            return dto;
        }
    }
}
