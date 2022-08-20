package com.dnd.niceteam.domain.recruiting.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class ApplicantNotFoundException extends BusinessException {

    public ApplicantNotFoundException(String message) {
        super(ErrorCode.APPLICANT_NOT_FOUND, message);
    }
}
