package com.dnd.niceteam.domain.department.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class DepartmentNotFoundException extends BusinessException {

    public DepartmentNotFoundException(String message) {
        super(ErrorCode.DEPARTMENT_NOT_FOUND, message);
    }
}
