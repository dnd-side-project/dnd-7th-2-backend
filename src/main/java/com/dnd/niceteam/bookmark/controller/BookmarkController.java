package com.dnd.niceteam.bookmark.controller;

import com.dnd.niceteam.bookmark.dto.BookmarkCreation;
import com.dnd.niceteam.bookmark.dto.BookmarkDeletion;
import com.dnd.niceteam.bookmark.service.BookmarkService;
import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.domain.bookmark.exception.BookmarkNotOwnedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<ApiResult<BookmarkDeletion.ResponseDto>> bookmarkDelete(@PathVariable long bookmarkId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!bookmarkService.isBookmarkOwnedByMember(bookmarkId, username)) {
            throw new BookmarkNotOwnedException(String.format(
                    "bookmarkId = %d, username = %s", bookmarkId, username));
        }
        BookmarkDeletion.ResponseDto responseDto = bookmarkService.deleteBookmark(bookmarkId);
        ApiResult<BookmarkDeletion.ResponseDto> apiResult = ApiResult.success(responseDto);
        return ResponseEntity.ok(apiResult);
    }
}
