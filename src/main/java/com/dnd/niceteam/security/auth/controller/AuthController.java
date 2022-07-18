package com.dnd.niceteam.security.auth.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.security.auth.dto.AuthRequestDto;
import com.dnd.niceteam.security.auth.dto.AuthResponseDto;
import com.dnd.niceteam.security.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResult<AuthResponseDto.TokenInfo>> login(
            @Valid @RequestBody AuthRequestDto.Login loginRequestDto) {
        AuthResponseDto.TokenInfo login = authService.login(loginRequestDto);
        ApiResult<AuthResponseDto.TokenInfo> apiResult = ApiResult.success(login);

        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Valid @RequestBody AuthRequestDto.Reissue reissueRequestDto) {
        AuthResponseDto.Reissue reissue = authService.reissueAccessToken(reissueRequestDto);
        ApiResult<AuthResponseDto.Reissue> apiResult = ApiResult.success(reissue);

        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

    // NOTICE. header에 Access Token
    @PostMapping("/logout")
    public ResponseEntity<?> logout(String username) {
        authService.logout(username);

        return new ResponseEntity<>(ApiResult.success(), HttpStatus.CREATED);
    }
}
