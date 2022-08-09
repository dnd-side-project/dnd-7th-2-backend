package com.dnd.niceteam.review.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.review.dto.MemberReviewRequest;
import com.dnd.niceteam.review.service.MemberReviewService;
import com.dnd.niceteam.security.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member-reviews")
@RequiredArgsConstructor
public class MemberReviewController {

    private final MemberReviewService memberReviewService;

    @PostMapping
    public ResponseEntity<ApiResult<Void>> memberReviewAdd(MemberReviewRequest.Add request, @CurrentMember Member currentMember) {
        memberReviewService.addMemberReview(request, currentMember);
        ApiResult<Void> apiResult = ApiResult.<Void>success().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

}
