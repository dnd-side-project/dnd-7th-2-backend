package com.dnd.niceteam.recruiting.dto;

import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.project.dto.LectureTimeRequest;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RecruitingModify {
    // 수정 (바뀌지 않은 것도 담아서 보내는 방식)
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
        @NotNull
        private String introLink;
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
    }

    @Data
    class ResponseDto {
        private Long recruitingId;
        private Long projectId;

        public static RecruitingModify.ResponseDto from(Recruiting recruiting) {
            RecruitingModify.ResponseDto dto = new RecruitingModify.ResponseDto();

            dto.setRecruitingId(recruiting.getId());
            dto.setProjectId(recruiting.getProject().getId());

            return dto;
        }
    }
}
