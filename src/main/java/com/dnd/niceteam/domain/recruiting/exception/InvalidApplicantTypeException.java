package com.dnd.niceteam.domain.recruiting.exception;

import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class InvalidApplicantTypeException extends BusinessException {
    public InvalidApplicantTypeException(RecruitingStatus status, Boolean joined) {
        super(ErrorCode.INVALID_APPLICANT_TYPE, "recruiting status = " + status + ", joined status = " + joined);
    }
}