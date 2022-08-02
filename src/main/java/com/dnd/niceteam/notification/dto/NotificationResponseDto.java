package com.dnd.niceteam.notification.dto;

import lombok.Data;

@Data
public class NotificationResponseDto {

    private String message;

    public NotificationResponseDto(String message) {
        this.message = message;
    }

}
