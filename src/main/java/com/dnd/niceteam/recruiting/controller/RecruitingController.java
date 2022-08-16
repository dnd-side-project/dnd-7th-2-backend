package com.dnd.niceteam.recruiting.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.service.RecruitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/recruiting")
@RestController
public class RecruitingController {
    private final RecruitingService recruitingService;

    @PostMapping
    public ResponseEntity<ApiResult<RecruitingCreation.ResponseDto>> recruitingAdd (@RequestBody RecruitingCreation.RequestDto recruitingReqDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        RecruitingCreation.ResponseDto responseDto = recruitingService.addProjectAndRecruiting(username, recruitingReqDto);

        ApiResult<RecruitingCreation.ResponseDto> apiResult = ApiResult.success(responseDto);
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

}
