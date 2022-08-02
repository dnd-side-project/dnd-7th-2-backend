package com.dnd.niceteam.notification.service;

import com.dnd.niceteam.notification.dto.NotificationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final FirebaseCloudMessagingService fcmService;

    public void send(NotificationRequestDto request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }

}
