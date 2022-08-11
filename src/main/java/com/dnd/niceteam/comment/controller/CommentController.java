package com.dnd.niceteam.comment.controller;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.comment.service.CommentService;
import com.dnd.niceteam.common.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruiting/{recruitingId}/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResult<CommentCreation.ResponseDto>> commentAdd(@PathVariable Long recruitingId,
                                                                             @RequestBody @Valid CommentCreation.RequestDto commentDto) {
        CommentCreation.ResponseDto responseDto = commentService.addComment(recruitingId, commentDto);

        ApiResult<CommentCreation.ResponseDto> apiResult = ApiResult.success(responseDto);
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }
}
