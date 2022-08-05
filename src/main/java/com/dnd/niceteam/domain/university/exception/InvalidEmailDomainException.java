package com.dnd.niceteam.domain.university.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class InvalidEmailDomainException extends BusinessException {

    public InvalidEmailDomainException(String message) {
        super(ErrorCode.INVALID_EMAIL_DOMAIN, message);
    }
}
