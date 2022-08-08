package com.dnd.niceteam.domain.member.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException(String message) {
        super(ErrorCode.DUPLICATE_EMAIL, message);
    }
}
