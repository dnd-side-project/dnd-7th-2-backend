package com.dnd.niceteam.vote.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class ExpiredVoteGroupException extends BusinessException {
    public ExpiredVoteGroupException(String message) {
        super(ErrorCode.EXPIRED_VOTE_GROUP, message);
    }
}
