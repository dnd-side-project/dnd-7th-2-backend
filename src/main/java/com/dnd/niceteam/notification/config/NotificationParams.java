package com.dnd.niceteam.notification.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationParams {
    SOUND("default")
    , COLOR("#FFFF00")
    ;

    private final String value;

}
