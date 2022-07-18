package com.dnd.niceteam.security.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class RefreshTokenIsNullException extends BusinessException {
    public RefreshTokenIsNullException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
