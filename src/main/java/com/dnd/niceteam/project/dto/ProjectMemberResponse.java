package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.project.ProjectMember;
import lombok.Data;

public interface ProjectMemberResponse {

    @Data
    class Summary {

        private long memberId;

        private String nickname;

        private int admissionYear;

        private PersonalityResponse personality;

        private boolean expelled;

        public static Summary from(ProjectMember projectMember) {
            Member member = projectMember.getMember();
            PersonalityResponse personalityResponse = PersonalityResponse.from(member.getPersonality());

            Summary dto = new Summary();

            dto.setMemberId(projectMember.getId());
            dto.setNickname(member.getNickname());
            dto.setAdmissionYear(member.getAdmissionYear());
            dto.setPersonality(personalityResponse);
            dto.setExpelled(projectMember.getExpelled());

            return dto;
        }

    }

}
