package com.dnd.niceteam.project.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class ProjectAlreadyDoneException extends BusinessException {
    public ProjectAlreadyDoneException(Long projectId) {
        super(ErrorCode.PROJECT_ALREADY_DONE, "projectId = " + projectId);
    }
}
