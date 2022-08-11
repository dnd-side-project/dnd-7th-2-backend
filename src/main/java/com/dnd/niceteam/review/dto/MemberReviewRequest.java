package com.dnd.niceteam.review.dto;

import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.review.MemberReview;
import com.dnd.niceteam.domain.review.MemberReviewTag;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

public interface MemberReviewRequest {

    @Data
    class Add {

        @Positive
        @Max(5)
        @NotNull
        private Integer participationScore;

        @Positive
        @Max(5)
        @NotNull
        private Integer teamAgainScore;

        @NotNull
        private List<String> tagNames;

        @NotNull
        private Long projectId;
        @NotNull
        private Long revieweeId;

        public MemberReview toEntity(ProjectMember reviewer, ProjectMember reviewee, Set<MemberReviewTag> memberReviewTags) {
            return MemberReview.builder()
                    .participationScore(participationScore)
                    .teamAgainScore(teamAgainScore)
                    .reviewer(reviewer)
                    .reviewee(reviewee)
                    .memberReviewTags(memberReviewTags)
                    .build();
        }

    }
}
