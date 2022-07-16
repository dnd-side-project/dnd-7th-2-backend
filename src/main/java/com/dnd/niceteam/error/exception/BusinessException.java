package com.dnd.niceteam.error.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BusinessException(ErrorCode errorCode, String message) {
        super(errorCode.name() + " : " + errorCode.getMessage() + " " + message);
        this.errorCode = errorCode;
    }
}
