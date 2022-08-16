package com.dnd.niceteam.domain.bookmark.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class BookmarkExistingException extends BusinessException {

    public BookmarkExistingException(String message) {
        super(ErrorCode.RECRUITING_NOT_FOUND, message);
    }
}
