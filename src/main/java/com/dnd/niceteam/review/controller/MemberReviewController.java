package com.dnd.niceteam.review.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.review.dto.MemberReviewRequest;
import com.dnd.niceteam.review.service.MemberReviewService;
import com.dnd.niceteam.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member-reviews")
@RequiredArgsConstructor
public class MemberReviewController {

    private final MemberReviewService memberReviewService;

    @PostMapping
    public ResponseEntity<ApiResult<Void>> memberReviewAdd(
            @RequestBody MemberReviewRequest.Add request,
            @CurrentUser User currentUser) {
        memberReviewService.addMemberReview(request, currentUser);
        ApiResult<Void> apiResult = ApiResult.<Void>success().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PostMapping("/skip")
    public ResponseEntity<ApiResult<Void>> memberReviewSkip(
            @RequestBody MemberReviewRequest.Skip request,
            @CurrentUser User currentUser) {
        memberReviewService.skipMemberReview(request, currentUser);
        ApiResult<Void> apiResult = ApiResult.<Void>success().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

}
