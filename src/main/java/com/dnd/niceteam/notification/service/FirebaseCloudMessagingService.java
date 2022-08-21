package com.dnd.niceteam.notification.service;

import com.dnd.niceteam.notification.util.FCMUtil;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FirebaseCloudMessagingService {

    public String sendMessage(Message message)
            throws InterruptedException, ExecutionException {
        return FCMUtil.sendToFirebase(message);
    }

}
