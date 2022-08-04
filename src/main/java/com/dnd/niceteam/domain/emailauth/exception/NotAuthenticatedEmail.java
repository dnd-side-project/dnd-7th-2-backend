package com.dnd.niceteam.domain.emailauth.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class NotAuthenticatedEmail extends BusinessException {

    public NotAuthenticatedEmail(String message) {
        super(ErrorCode.NOT_AUTHENTICATED_EMAIL, message);
    }
}
