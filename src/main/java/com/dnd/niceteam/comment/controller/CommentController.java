package com.dnd.niceteam.comment.controller;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.comment.dto.CommentFind;
import com.dnd.niceteam.comment.dto.CommentModify;
import com.dnd.niceteam.comment.service.CommentService;
import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.security.CurrentUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruiting")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{recruitingId}/comment")
    public ResponseEntity<ApiResult<CommentCreation.ResponseDto>> commentAdd(@PathVariable Long recruitingId,
                                                                             @RequestBody @Valid CommentCreation.RequestDto requestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CommentCreation.ResponseDto responseDto = commentService.addComment(recruitingId, username, requestDto);

        ApiResult<CommentCreation.ResponseDto> apiResult = ApiResult.success(responseDto);
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

    @GetMapping("/{recruitingId}/comment")
    public ResponseEntity<ApiResult<Pagination<CommentFind.ResponseDto>>> commentList(@PathVariable Long recruitingId,
                                                                                      @RequestParam(defaultValue = "1", required = false) Integer page,
                                                                                      @RequestParam(defaultValue = "10", required = false) Integer perSize) {
        Pagination<CommentFind.ResponseDto> comments = commentService.getComments(page, perSize, recruitingId, null);

        ApiResult<Pagination<CommentFind.ResponseDto>> apiResult = ApiResult.success(comments);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/comment/me")
    public ResponseEntity<ApiResult<Pagination<CommentFind.ResponseDto>>> myCommentList(@RequestParam(defaultValue = "1", required = false) Integer page,
                                                                                        @RequestParam(defaultValue = "10", required = false) Integer perSize,
                                                                                        @CurrentUsername String username) {
        Pagination<CommentFind.ResponseDto> comments = commentService.getComments(page, perSize, null, username);

        ApiResult<Pagination<CommentFind.ResponseDto>> apiResult = ApiResult.success(comments);
        return ResponseEntity.ok(apiResult);
    }
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<ApiResult<Void>> commentRemove (@PathVariable Long commentId) {
        commentService.removeComment(commentId);

        ApiResult<Void> apiResult = ApiResult.<Void>success().build();
        return ResponseEntity.ok(apiResult);
    }

    @PutMapping("/comment")
    public ResponseEntity<ApiResult<CommentModify.ResponseDto>> commentModify(@RequestBody @Valid CommentModify.RequestDto requestDto) {
        CommentModify.ResponseDto comments = commentService.modifyComment(requestDto);
        ApiResult<CommentModify.ResponseDto> apiResult = ApiResult.success(comments);
        return ResponseEntity.ok(apiResult);
    }
}
