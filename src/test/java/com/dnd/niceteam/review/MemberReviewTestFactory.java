package com.dnd.niceteam.review;

import com.dnd.niceteam.domain.code.ReviewTag;
import com.dnd.niceteam.review.dto.MemberReviewRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MemberReviewTestFactory {

    public static MemberReviewRequest.Add getAddRequest(Long revieweeId) {
        MemberReviewRequest.Add request = new MemberReviewRequest.Add();
        Set<ReviewTag> reviewTags = new HashSet<>(List.of(ReviewTag.RESPONSIBILITY, ReviewTag.PUNCTUALITY));

        request.setParticipationScore(5);
        request.setTeamAgainScore(4);
        request.setProjectId(1L);
        request.setRevieweeId(revieweeId);
        request.setReviewTags(reviewTags);

        return request;
    }

    public static MemberReviewRequest.Add getAddRequest() {
        return getAddRequest(2L);
    }

    public static MemberReviewRequest.Skip getSkipRequest(Long revieweeId) {
        MemberReviewRequest.Skip request = new MemberReviewRequest.Skip();

        request.setProjectId(1L);
        request.setRevieweeId(revieweeId);

        return request;
    }

    public static MemberReviewRequest.Skip getSkipRequest() {
        return getSkipRequest(2L);
    }


}
