package com.dnd.niceteam.project.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class ProjectMemberAlreadyJoinedException extends BusinessException {
    public ProjectMemberAlreadyJoinedException(Long projectId, Long applicantMemberId) {
        super(
                ErrorCode.PROJECT_MEMBER_ALREADY_JOINED,
                String.format("projectId = %d, memberId = %d", projectId, applicantMemberId)
        );
    }

}
