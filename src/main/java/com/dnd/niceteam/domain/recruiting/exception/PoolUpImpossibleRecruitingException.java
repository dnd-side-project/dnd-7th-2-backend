package com.dnd.niceteam.domain.recruiting.exception;

import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class PoolUpImpossibleRecruitingException extends BusinessException {
    public PoolUpImpossibleRecruitingException(RecruitingStatus status) {
        super(ErrorCode.POOL_UP_IMPOSSIBLE_RECRUITING, "recruiting status = " + status);
    }
}
