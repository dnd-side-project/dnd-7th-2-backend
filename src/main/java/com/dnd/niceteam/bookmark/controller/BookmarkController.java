package com.dnd.niceteam.bookmark.controller;

import com.dnd.niceteam.bookmark.dto.BookmarkCreation;
import com.dnd.niceteam.bookmark.service.BookmarkService;
import com.dnd.niceteam.common.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/bookmarks")
@RequiredArgsConstructor
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{recruitingId}")
    public ResponseEntity<ApiResult<BookmarkCreation.ResponseDto>> bookmarkCreate(@PathVariable long recruitingId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        BookmarkCreation.ResponseDto responseDto = bookmarkService.createBookmark(username, recruitingId);
        ApiResult<BookmarkCreation.ResponseDto> apiResult = ApiResult.success(responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }
}
