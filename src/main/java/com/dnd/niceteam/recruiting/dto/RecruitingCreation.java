package com.dnd.niceteam.recruiting.dto;

import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.department.Department;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public interface RecruitingCreation {
    @Data
    class RequestDto {
        @NotNull
        private Long memberId;
        @NotNull
        private Long projectId;
        @NotNull
        private String title;
        @NotNull
        private String content;
        @NotNull
        private LocalDate projectStartDate;
        @NotNull
        private LocalDate projectEndDate;
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
        private String lectureName;
        @Nullable
        private String professor;
        @Nullable
        private Department department;

        @Nullable
        private Field field;
        @Nullable
        private FieldCategory fieldCategory;

    }
    @Data
    class ResponseDto {
        @NotNull
        private Long id;
    }
}
