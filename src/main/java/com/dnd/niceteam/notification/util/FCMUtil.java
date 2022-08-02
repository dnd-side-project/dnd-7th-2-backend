package com.dnd.niceteam.notification.util;

import com.dnd.niceteam.notification.config.NotificationParams;
import com.dnd.niceteam.notification.dto.NotificationRequestDto;
import com.google.firebase.messaging.*;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

public final class FCMUtil {

    private FCMUtil() {}

    public static Notification buildNotification(NotificationRequestDto request) {
        return Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getMessage())
                .build();
    }

    public static Message buildMessage(NotificationRequestDto request) {
        Notification notification = buildNotification(request);
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());

        Message.Builder messageBuilder = Message.builder();

        if (!request.getData().isEmpty()) messageBuilder.putAllData(request.getData());

        // 토큰이 있으면 토큰을, 없으면 Topic을 주입
        if (request.getToken() != null) messageBuilder.setToken(request.getToken());
        else messageBuilder.setTopic(request.getTopic());

        return messageBuilder
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .build();
    }

    public static String sendToFirebase(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    /* Android 관련 메서드 */
    private static AndroidConfig getAndroidConfig(String topic) {
        AndroidNotification notification = getAndroidNotification(topic);

        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(notification)
                .build();
    }

    private static AndroidNotification getAndroidNotification(String topic) {
        return AndroidNotification.builder()
                .setSound(NotificationParams.SOUND.getValue())
                .setColor(NotificationParams.COLOR.getValue())
                .setTag(topic)
                .build();
    }

}
