package com.dnd.niceteam.recruiting.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class OnlyRecruiterAvailableException extends BusinessException {
    public OnlyRecruiterAvailableException(Long currentMemberId, Long recruitingId) {
        super(
                ErrorCode.ONLY_RECRUITER_AVAILABLE,
                String.format("currentMemberId = %d, recruitingId = %d", currentMemberId, recruitingId)
        );
    }
}
