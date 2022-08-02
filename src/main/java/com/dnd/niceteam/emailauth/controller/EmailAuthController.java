package com.dnd.niceteam.emailauth.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.emailauth.dto.EmailAuthKeyCheckRequestDto;
import com.dnd.niceteam.emailauth.dto.EmailAuthKeyCheckResponseDto;
import com.dnd.niceteam.emailauth.dto.EmailAuthKeySendRequestDto;
import com.dnd.niceteam.emailauth.service.EmailAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/email-auth")
@RestController
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    @PostMapping("/send")
    public ResponseEntity<ApiResult<Void>> emailAuthKeySend(@Valid @RequestBody EmailAuthKeySendRequestDto requestDto) {
        emailAuthService.sendEmailAuthKey(requestDto);
        return ResponseEntity.ok(ApiResult.<Void>success().build());
    }

    @PostMapping("/check")
    public ResponseEntity<ApiResult<EmailAuthKeyCheckResponseDto>> emailAuthKeyCheck(
            @Valid @RequestBody EmailAuthKeyCheckRequestDto requestDto) {
        EmailAuthKeyCheckResponseDto responseDto = emailAuthService.checkEmailAuthKey(requestDto);
        ApiResult<EmailAuthKeyCheckResponseDto> apiResult = ApiResult.success(responseDto);
        return ResponseEntity.ok(apiResult);
    }
}
