package com.dnd.niceteam.vote.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class SelfVoteException extends BusinessException {
    public SelfVoteException(Long memberId) {
        super(ErrorCode.SELF_VOTE, "memberId = " + memberId);
    }
}
