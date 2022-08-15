package com.dnd.niceteam.recruiting.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class ApplyCancelImpossibleRecruitingException extends BusinessException {

    public ApplyCancelImpossibleRecruitingException(String message) {
        super(ErrorCode.APPLY_CANCEL_IMPOSSIBLE_RECRUITING, message);
    }
}
