package com.dnd.niceteam.member.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
