package com.dnd.niceteam.error.exception;

public class TokenInvalidException extends BusinessException{
    public TokenInvalidException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
