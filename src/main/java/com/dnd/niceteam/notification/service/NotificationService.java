package com.dnd.niceteam.notification.service;

import com.dnd.niceteam.notification.dto.NotificationRequestDto;
import com.dnd.niceteam.notification.util.FCMUtil;
import com.google.firebase.messaging.Message;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NotificationService {

    private final FirebaseCloudMessagingService fcmService;

    public void send(NotificationRequestDto request) throws ExecutionException, InterruptedException {
            Message message = FCMUtil.buildMessage(request);
            fcmService.sendMessage(message);
    }

}
