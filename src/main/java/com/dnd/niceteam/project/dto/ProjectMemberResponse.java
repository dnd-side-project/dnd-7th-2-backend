package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.review.MemberReview;
import lombok.Data;

import java.util.Objects;

public interface ProjectMemberResponse {

    @Data
    class Summary {

        private long memberId;

        private String nickname;

        private int admissionYear;

        private PersonalityResponse personality;

        private boolean expelled;

        private boolean reviewed = false;

        private boolean me;

        public static Summary from (ProjectMember projectMember, Long currentMemberId) {
            return from(projectMember, currentMemberId, null);
        }

        public static Summary from(ProjectMember projectMember, Long currentMemberId, MemberReview memberReview) {
            Member member = projectMember.getMember();
            PersonalityResponse personalityResponse = PersonalityResponse.from(member.getPersonality());

            Summary dto = new Summary();

            dto.setMemberId(projectMember.getMember().getId());
            dto.setNickname(member.getNickname());
            dto.setAdmissionYear(member.getAdmissionYear());
            dto.setPersonality(personalityResponse);
            dto.setExpelled(projectMember.getExpelled());

            if (memberReview != null) {
                boolean isMemberReviewed = projectMember.equals(memberReview.getReviewee());
                dto.setReviewed(isMemberReviewed);
            }

            boolean isMe = Objects.equals(currentMemberId, projectMember.getMember().getId());
            dto.setMe(isMe);

            return dto;
        }

    }

}
