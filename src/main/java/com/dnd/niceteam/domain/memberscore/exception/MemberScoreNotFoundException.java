package com.dnd.niceteam.domain.memberscore.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class MemberScoreNotFoundException extends BusinessException {

    public MemberScoreNotFoundException(String message) {
        super(ErrorCode.MEMBER_SCORE_NOT_FOUND, message);
    }
}
