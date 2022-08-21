package com.dnd.niceteam.notification.service;

import com.dnd.niceteam.notification.dto.NotificationRequestDto;
import com.google.firebase.messaging.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;

    @Mock
    FirebaseCloudMessagingService fcmService;

    @Test
    void 푸쉬_알람_전송() throws ExecutionException, InterruptedException {
        // given
        NotificationRequestDto request = new NotificationRequestDto();
        request.setTitle("팀원 수락");
        request.setMessage("DND 7기 2조에 합류하신걸 축하합니다!");

        when(fcmService.sendMessage(any(Message.class))).thenReturn("푸쉬 알람 전송 성공");

        // when
        notificationService.send(request);

        // then
        verify(fcmService).sendMessage(any(Message.class));
    }


}