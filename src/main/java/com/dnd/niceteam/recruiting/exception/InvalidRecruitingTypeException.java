package com.dnd.niceteam.recruiting.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class InvalidRecruitingTypeException extends BusinessException {

    public InvalidRecruitingTypeException(String message) {
        super(ErrorCode.INVALID_RECRUITING_TYPE, message);
    }
}
