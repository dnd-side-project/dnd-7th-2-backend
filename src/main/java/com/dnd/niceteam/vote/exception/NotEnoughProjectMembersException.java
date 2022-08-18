package com.dnd.niceteam.vote.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class NotEnoughProjectMembersException extends BusinessException {
    public NotEnoughProjectMembersException(Long id) {
        super(ErrorCode.NOT_ENOUGH_PROJECT_MEMBER, "projectId = " + id);
    }
}
