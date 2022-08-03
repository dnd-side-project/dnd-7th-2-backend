package com.dnd.niceteam.domain.university.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class UniversityNotFoundException extends BusinessException {

    public UniversityNotFoundException(String message) {
        super(ErrorCode.UNIVERSITY_NOT_FOUND, message);
    }
}
