package com.dnd.niceteam.notification.dto;

import lombok.Data;

import java.util.Map;

@Data
public class NotificationRequestDto {

    private String title;

    private String message;

    private String topic;

    private String token;

    private Map<String, String> data;

}
