package com.dnd.niceteam.domain.recruiting.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class ApplyImpossibleRecruitingException extends BusinessException {

    public ApplyImpossibleRecruitingException(String message) {
        super(ErrorCode.APPLY_IMPOSSIBLE_RECRUITING, message);
    }
}
