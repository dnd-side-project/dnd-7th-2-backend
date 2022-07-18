package com.dnd.niceteam.security.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class RefreshTokenIsNullException extends BusinessException {
    public RefreshTokenIsNullException(String message) {
        super(ErrorCode.REFRESH_TOKEN_IS_NULL_ERROR, message);
    }
}
