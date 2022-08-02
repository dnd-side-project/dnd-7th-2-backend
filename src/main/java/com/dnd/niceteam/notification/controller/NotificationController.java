package com.dnd.niceteam.notification.controller;

import com.dnd.niceteam.notification.dto.NotificationRequestDto;
import com.dnd.niceteam.notification.dto.NotificationResponseDto;
import com.dnd.niceteam.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponseDto> send(@RequestBody NotificationRequestDto request) {
        try {
            notificationService.send(request);
            log.info("Send notification successfully");
            return ResponseEntity.ok(new NotificationResponseDto("푸쉬 알림이 전송되었습니다."));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
