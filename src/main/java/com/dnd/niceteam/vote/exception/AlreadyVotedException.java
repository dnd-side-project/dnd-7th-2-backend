package com.dnd.niceteam.vote.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class AlreadyVotedException extends BusinessException {

    public AlreadyVotedException(Long voteGroupId, Long voterId) {
        super(ErrorCode.ALREADY_VOTED, String.format("voteGroupId = %d, voterId = %d", voteGroupId, voterId));
    }

}