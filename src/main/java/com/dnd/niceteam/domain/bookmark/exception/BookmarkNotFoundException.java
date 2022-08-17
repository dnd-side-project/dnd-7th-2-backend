package com.dnd.niceteam.domain.bookmark.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class BookmarkNotFoundException extends BusinessException {

    public BookmarkNotFoundException(String message) {
        super(ErrorCode.BOOKMARK_NOT_FOUND, message);
    }
}
