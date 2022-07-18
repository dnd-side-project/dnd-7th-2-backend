package com.dnd.niceteam.error.exception;

public class MemberNotFoundException extends BusinessException{
    public MemberNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
