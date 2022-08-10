package com.dnd.niceteam.recruiting.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class InvalidRecruitingType extends BusinessException {

    public InvalidRecruitingType(String message) {
        super(ErrorCode.INVALID_RECRUITING_TYPE, message);
    }
}
