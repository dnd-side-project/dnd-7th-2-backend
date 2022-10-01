package com.dnd.niceteam.recruiting.dto;

import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.recruiting.ActivityDayTime;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
import com.dnd.niceteam.project.dto.LectureTimeRequest;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// TODO: 2022-08-22 Validation 추가 필요 
public interface RecruitingCreation {
    @Data
    class RequestDto {
        @NotBlank(message = "제목에 텍스트를 입력해주세요.")
        @Size(max = 30, message = "제목은 30자 이내로 입력해주세요.")
        private String title;
        @NotNull
        @Size(max = 400, message = "400자 이내로 입력해주세요.")
        private String content;
        @NotNull
        private Integer recruitingMemberCount;
        @NotNull
        private Type recruitingType;
        @NotNull
        private ActivityArea activityArea;
        @NotNull
        private String introLink;
        @NotNull
        private RecruitingStatus status;
        @FutureOrPresent
        @Nullable
        private LocalDate recruitingEndDate;    // 기간이 정해져있지 않으면 null
        @NotNull
        private Set<ActivityDayTimeDto> activityDayTimes;

        @NotNull
        private Set<Personality.Adjective> personalityAdjectives;
        @NotNull
        private Set<Personality.Noun> personalityNouns;

        @NotNull
        private LocalDate projectStartDate;
        @NotNull
        private LocalDate projectEndDate;
        @NotNull
        private String projectName;

        @Nullable
        private List<LectureTimeRequest> lectureTimes;
        @Nullable
        private String professor;
        @Nullable
        private Long departmentId;

        @Nullable
        private Field field;
        @Nullable
        private FieldCategory fieldCategory;

        public Recruiting toEntity(Project project, Member member) {
            return Recruiting.builder()
                    .project(project)
                    .member(member)
                    .title(title)
                    .content(content)
                    .recruitingMemberCount(recruitingMemberCount)
                    .recruitingType(recruitingType)
                    .activityArea(activityArea)
                    .activityDayTimes(convertToEntity(activityDayTimes))
                    .recruitingEndDate(recruitingEndDate)
                    .bookmarkCount(0)
                    .commentCount(0)
                    .poolUpCount(0)
                    .introLink(introLink)
                    .personalityAdjectives(personalityAdjectives)
                    .personalityNouns(personalityNouns)
                    .build();
        }

        private Set<ActivityDayTime> convertToEntity(Set<ActivityDayTimeDto> activityDayTimes) {
            return activityDayTimes.stream()
                    .map(ActivityDayTimeDto::toEntity).collect(Collectors.toSet());
        }
    }

    @Data
    class ResponseDto {
        @NotNull
        private Long recruitingId;

        @NotNull
        private Long projectId;

        public static RecruitingCreation.ResponseDto from(Recruiting recruiting) {
            ResponseDto dto = new ResponseDto();

            dto.setRecruitingId(recruiting.getId());
            dto.setProjectId(recruiting.getProject().getId());

            return dto;
        }
    }
}
