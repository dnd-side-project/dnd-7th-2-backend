package com.dnd.niceteam.notification.exception;

import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;

public class NotificationPushFailedException extends BusinessException {

    public NotificationPushFailedException(String title, String message) {
        super(ErrorCode.NOTIFICATION_PUSH_FAILED, String.format("title = %s, message = %s", title, message));
    }
}
