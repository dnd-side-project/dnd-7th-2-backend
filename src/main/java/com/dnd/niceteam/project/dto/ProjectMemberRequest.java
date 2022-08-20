package com.dnd.niceteam.project.dto;

import lombok.Data;

public interface ProjectMemberRequest {

    @Data
    class Add {

        private Long applicantMemberId;

        private Long recruitingId;

    }

}
