package com.dnd.niceteam.applicant.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

public interface ApplicantCreation {
    @Data
    class ResponseDto {
        @NotNull
        private Long id;
    }
}
