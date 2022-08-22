package com.dnd.niceteam.project.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class ProjectMemberNotFoundException extends BusinessException {

    public ProjectMemberNotFoundException(Long projectId, Long memberId) {
        super(
                ErrorCode.PROJECT_MEMBER_NOT_FOUND,
                String.format("projectId =  %d, memberId = %d", projectId, memberId)
        );
    }
}
