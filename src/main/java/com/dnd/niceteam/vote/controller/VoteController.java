package com.dnd.niceteam.vote.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.security.CurrentUser;
import com.dnd.niceteam.vote.dto.VoteRequest;
import com.dnd.niceteam.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<ApiResult<Void>> voteAdd(
            @RequestBody VoteRequest.Add request,
            @CurrentUser User currentUser
    ) {
        voteService.addVote(request, currentUser);
        ApiResult<Void> apiResult = ApiResult.<Void>success().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

}
