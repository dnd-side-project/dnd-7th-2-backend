package com.dnd.niceteam.review.dto;

import com.dnd.niceteam.domain.code.ReviewTag;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.review.MemberReview;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

public interface MemberReviewRequest {

    Long getProjectId();

    Long getRevieweeId();

    @Data
    class Add implements MemberReviewRequest {

        @Positive
        @Max(5)
        @NotNull
        private Integer participationScore;

        @Positive
        @Max(5)
        @NotNull
        private Integer teamAgainScore;

        @NotNull
        private Set<ReviewTag> reviewTags;

        @NotNull
        private Long projectId;
        @NotNull
        private Long revieweeId;

        public MemberReview toEntity(ProjectMember reviewer, ProjectMember reviewee) {
            return MemberReview.builder()
                    .participationScore(participationScore)
                    .teamAgainScore(teamAgainScore)
                    .reviewer(reviewer)
                    .reviewee(reviewee)
                    .reviewTags(reviewTags)
                    .build();
        }


    }

    @Data
    class Skip implements MemberReviewRequest {

        @NotNull
        private Long projectId;

        @NotNull
        private Long revieweeId;

    }
}
