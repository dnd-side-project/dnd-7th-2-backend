package com.dnd.niceteam.security.auth.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.security.auth.dto.AuthRequestDto;
import com.dnd.niceteam.security.auth.dto.AuthResponseDto;
import com.dnd.niceteam.security.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/reissue")
    public ResponseEntity<ApiResult<AuthResponseDto.TokenInfo>> reissue(
            @Valid @RequestBody AuthRequestDto.Reissue reissueRequestDto) {
        AuthResponseDto.TokenInfo reissue = authService.reissueAccessToken(reissueRequestDto);
        ApiResult<AuthResponseDto.TokenInfo> apiResult = ApiResult.success(reissue);

        return ResponseEntity.ok(apiResult);
    }
}
