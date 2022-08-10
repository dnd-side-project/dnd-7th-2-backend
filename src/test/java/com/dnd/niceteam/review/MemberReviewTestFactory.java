package com.dnd.niceteam.review;

import com.dnd.niceteam.domain.review.MemberReviewTagName;
import com.dnd.niceteam.review.dto.MemberReviewRequest;

import java.util.ArrayList;
import java.util.List;

public class MemberReviewTestFactory {

    public static MemberReviewRequest.Add getMemberReviewRequest(Long revieweeId) {
        MemberReviewRequest.Add request = new MemberReviewRequest.Add();
        List<String> tagNames = new ArrayList<>(List.of(MemberReviewTagName.책임감_굿.getKor(), MemberReviewTagName.마감을_칼같이.getKor()));

        request.setParticipationScore(5);
        request.setHopeToReunionScore(4);
        request.setProjectId(1L);
        request.setRevieweeId(revieweeId);
        request.setTagNames(tagNames);

        return request;
    }

    public static MemberReviewRequest.Add getMemberReviewRequest() {
        return getMemberReviewRequest(2L);
    }

}
