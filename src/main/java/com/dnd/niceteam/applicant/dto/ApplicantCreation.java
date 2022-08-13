package com.dnd.niceteam.applicant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public interface ApplicantCreation {
    @Data
    class RequestDto {
        @NotNull
        private Long memberId;
    }
    @AllArgsConstructor
    @Getter
    class ResponseDto {
        @NotNull
        private Long applicantId;
    }
}
