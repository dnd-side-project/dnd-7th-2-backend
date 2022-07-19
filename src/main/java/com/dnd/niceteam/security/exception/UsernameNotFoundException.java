package com.dnd.niceteam.security.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class UsernameNotFoundException extends BusinessException {

    public UsernameNotFoundException(String message) {
        super(ErrorCode.USERNAME_NOT_FOUND, message);
    }
}
