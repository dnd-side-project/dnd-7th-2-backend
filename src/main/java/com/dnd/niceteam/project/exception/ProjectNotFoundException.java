package com.dnd.niceteam.project.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class ProjectNotFoundException extends BusinessException {

    public ProjectNotFoundException(String message) {
        super(ErrorCode.PROJECT_NOT_FOUND, message);
    }
}
