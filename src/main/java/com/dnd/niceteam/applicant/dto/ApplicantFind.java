package com.dnd.niceteam.applicant.dto;

import com.dnd.niceteam.domain.recruiting.Applicant;
import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
import lombok.Data;

public interface ApplicantFind {
    @Data
    class ListRequestDto {
        RecruitingStatus recruitingStatus;
        Boolean applicantJoined;
    }

    @Data
    class ListResponseDto {
        Long recruitingId;
        RecruitingStatus recruitingStatus;
        Boolean joined;
        String projectName;
        Integer recruitingMemberCount;

        public static ListResponseDto from (Applicant applicant) {
            ListResponseDto dto = new ListResponseDto();
            dto.setRecruitingId(applicant.getRecruiting().getId());
            dto.setRecruitingStatus(applicant.getRecruiting().getStatus());
            dto.setJoined(applicant.getJoined());
            dto.setProjectName(applicant.getRecruiting().getProject().getName());   // 모집글의 제목은 어떨지
            dto.setRecruitingMemberCount(applicant.getRecruiting().getRecruitingMemberCount());
            return dto;
        }
    }
}
