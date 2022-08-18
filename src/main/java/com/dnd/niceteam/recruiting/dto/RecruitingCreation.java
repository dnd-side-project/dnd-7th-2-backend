package com.dnd.niceteam.recruiting.dto;

import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.project.dto.LectureTimeRequest;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RecruitingCreation {
    @Data
    class RequestDto {
        @NotNull
        private String title;
        @NotNull
        private String content;
        @NotNull
        private Integer recruitingMemberCount;
        @NotNull
        private Type recruitingType;
        @NotNull
        private ActivityArea activityArea;
        @Nullable
        private String introLink;
        @NotNull
        private ProgressStatus status;
        @Nullable
        private LocalDate recruitingEndDate;    // 기간이 정해져있지 않으면 null

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
                    .recruitingEndDate(recruitingEndDate)
                    .bookmarkCount(0)
                    .commentCount(0)
                    .poolUpCount(0)
                    .poolUpDate(null)
                    .introLink(introLink)
                    .personalityAdjectives(personalityAdjectives)
                    .personalityNouns(personalityNouns)
                    .build();
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
