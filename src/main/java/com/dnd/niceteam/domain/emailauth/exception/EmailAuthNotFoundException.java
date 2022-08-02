package com.dnd.niceteam.domain.emailauth.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class EmailAuthNotFoundException extends BusinessException {

    public EmailAuthNotFoundException(String message) {
        super(ErrorCode.EMAIL_AUTH_NOT_FOUND, message);
    }
}
