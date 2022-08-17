package com.dnd.niceteam.domain.bookmark.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class BookmarkNotOwnedException extends BusinessException {

    public BookmarkNotOwnedException(String message) {
        super(ErrorCode.BOOKMARK_NOT_OWNED, message);
    }
}
