package com.dnd.niceteam.error.exception;

public class RefreshTokenIsNullException extends BusinessException{
    public RefreshTokenIsNullException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
