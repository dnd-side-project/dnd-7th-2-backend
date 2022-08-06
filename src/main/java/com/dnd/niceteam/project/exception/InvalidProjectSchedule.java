package com.dnd.niceteam.project.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class InvalidProjectSchedule extends BusinessException {

    public InvalidProjectSchedule(String message) {
        super(ErrorCode.INVALID_PROJECT_SCHEDULE, message);
    }

}
