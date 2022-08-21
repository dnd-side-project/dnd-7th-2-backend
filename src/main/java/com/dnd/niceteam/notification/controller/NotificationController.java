package com.dnd.niceteam.notification.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.notification.dto.NotificationRequestDto;
import com.dnd.niceteam.notification.dto.NotificationResponseDto;
import com.dnd.niceteam.notification.exception.NotificationPushFailedException;
import com.dnd.niceteam.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<ApiResult<NotificationResponseDto>> send(@RequestBody NotificationRequestDto request) {
        try {
            notificationService.send(request);
            log.info("Send notification successfully");
            ApiResult<NotificationResponseDto> apiResult = ApiResult.success(new NotificationResponseDto("푸쉬 알림이 전송되었습니다."));
            return ResponseEntity.ok(apiResult);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NotificationPushFailedException(request.getTitle(), request.getMessage());
        }
    }

}
