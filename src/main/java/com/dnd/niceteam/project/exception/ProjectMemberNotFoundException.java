package com.dnd.niceteam.project.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class ProjectMemberNotFoundException extends BusinessException {

    public ProjectMemberNotFoundException(String message) {
        super(ErrorCode.PROJECT_MEMBER_NOT_FUND, message);
    }
}
