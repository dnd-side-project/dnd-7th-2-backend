package com.dnd.niceteam.applicant.controller;

import com.dnd.niceteam.applicant.dto.ApplicantCreation;
import com.dnd.niceteam.applicant.service.ApplicantService;
import com.dnd.niceteam.common.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruiting/{recruitingId}/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;

    @PostMapping
    public ResponseEntity<ApiResult<ApplicantCreation.ResponseDto>> applicantAdd (@PathVariable @NotNull Long recruitingId,
                                                         @RequestBody @Valid ApplicantCreation.RequestDto requestDto) {
        ApplicantCreation.ResponseDto responseDto = applicantService.addApplicant(recruitingId, requestDto.getMemberId());

        return new ResponseEntity<>(ApiResult.success(responseDto), HttpStatus.CREATED);
    }

}
