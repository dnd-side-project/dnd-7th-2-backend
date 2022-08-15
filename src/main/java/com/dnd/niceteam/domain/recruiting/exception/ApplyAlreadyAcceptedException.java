package com.dnd.niceteam.applicant.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class AlreadyJoinedException extends BusinessException {

    public AlreadyJoinedException(String message) {
        super(ErrorCode.APPLY_ALREADY_JOINED, message);
    }
}
