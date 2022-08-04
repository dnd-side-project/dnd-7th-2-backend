package com.dnd.niceteam.domain.member.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class DuplicateNicknameException extends BusinessException {

    public DuplicateNicknameException(String message) {
        super(ErrorCode.DUPLICATE_NICKNAME, message);
    }
}
