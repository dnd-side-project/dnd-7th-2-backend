package com.dnd.niceteam.domain.recruiting.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class RecruitingNotFoundException extends BusinessException {
    public RecruitingNotFoundException(String message) {
        super(ErrorCode.RECRUITING_NOT_FOUND, message);
    }

}
