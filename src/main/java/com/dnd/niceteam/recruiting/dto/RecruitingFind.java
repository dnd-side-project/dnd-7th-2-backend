package com.dnd.niceteam.recruiting.dto;

import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.SideProject;
import com.dnd.niceteam.domain.recruiting.ActivityDayTime;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.exception.InvalidRecruitingTypeException;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public interface RecruitingFind {
    @Data
    class DetailResponseDto {  // 상세 조회
        @NotNull private String memberNickname;
        @NotNull private String title;
        @NotNull private String content;
        @NotNull private Type recruitingType;
        @NotNull private ProgressStatus recruitingStatus;
        @NotNull private Integer commentCount;
        @NotNull private Integer bookmarkCount;
        @NotNull private String introLink;

        @NotNull private Integer recruitingMemberCount;
        @NotNull private ActivityArea activityArea;
        @NotNull private Set<Personality.Adjective> personalityAdjectives;
        @NotNull private Set<Personality.Noun> personalityNouns;
        private LocalDate recruitingEndDate;    // 기간이 정해져있지 않으면 null
        @NotNull private Set<ActivityDayTimeDto> activityDayTimes;
        @NotNull private Boolean isBookmarked;
        @NotNull private LocalDateTime recruitingCreatedDate;

        @JsonIgnoreProperties(value = {"memberList", "memberCount"})
        @NotNull private ProjectResponse.Detail projectResponse;

        public static RecruitingFind.DetailResponseDto from(Recruiting recruiting, boolean isBookmarked) {
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
            dto.setActivityDayTimes(convertToDto(recruiting.getActivityDayTimes()));
            dto.setPersonalityAdjectives(recruiting.getPersonalityAdjectives());
            dto.setPersonalityNouns(recruiting.getPersonalityNouns());
            dto.setRecruitingEndDate(recruiting.getRecruitingEndDate());
            dto.setIsBookmarked(isBookmarked);
            dto.setRecruitingCreatedDate(recruiting.getCreatedDate());

            ProjectResponse.Detail projectDto = ProjectResponse.Detail.from(recruiting.getProject());
            dto.setProjectResponse(projectDto);

            return dto;
        }

        private static Set<ActivityDayTimeDto> convertToDto(Set<ActivityDayTime> activityDayTimes) {
            return activityDayTimes.stream()
                    .map(ActivityDayTimeDto::from).collect(Collectors.toSet());
        }
    }

    @Data
    class ListResponseDto {
        // 목록 조회
        @NotNull private Long id;
        @NotNull private String title;
        @NotNull private Type type;
        @NotNull private ProgressStatus status;
        @NotNull private Integer commentCount;
        @NotNull private Integer bookmarkCount;
        @NotNull private String projectName;

        private String professor;

        private Field field;
        private FieldCategory fieldCategory;
        // 내가 쓴글 조회
        private LocalDateTime createdDate;

        public static ListResponseDto fromMyList(Recruiting recruiting) {
            ListResponseDto dto = createCommonListResponseDto(recruiting);

            dto.setCreatedDate(recruiting.getCreatedDate());
            return dto;
        }

        private static ListResponseDto createCommonListResponseDto(Recruiting recruiting) {
            ListResponseDto dto = new ListResponseDto();
            dto.setId(recruiting.getId());
            dto.setTitle(recruiting.getTitle());
            dto.setType(recruiting.getRecruitingType());
            dto.setStatus(recruiting.getStatus());
            dto.setCommentCount(recruiting.getCommentCount());
            dto.setBookmarkCount(recruiting.getBookmarkCount());
            dto.setProjectName(recruiting.getProject().getName());

            // TODO: 2022-08-16 Factory 클래스에 구분 메서드 (리팩토링)?
            switch (recruiting.getRecruitingType()) {
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
                    throw new InvalidRecruitingTypeException("Invalid Type: " + recruiting.getRecruitingType());
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

        public static RecommendedListResponseDto fromRecommendedList(Recruiting recruiting) {
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
