package com.dnd.niceteam.recruiting.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class ApplyImpossibleRecruitingStatusException extends BusinessException {

    public ApplyImpossibleRecruitingStatusException(String message) {
        super(ErrorCode.APPLY_IMPOSSIBLE_RECRUITING_STATUS, message);
    }
}
