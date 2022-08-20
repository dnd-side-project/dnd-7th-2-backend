package com.dnd.niceteam.applicant.controller;

import com.dnd.niceteam.applicant.dto.ApplicantCreation;
import com.dnd.niceteam.applicant.service.ApplicantService;
import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.security.CurrentUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruiting/{recruitingId}/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;

    @PostMapping
    public ResponseEntity<ApiResult<ApplicantCreation.ResponseDto>> applicantAdd (@PathVariable @NotNull Long recruitingId,
                                                                                  @CurrentUsername String username) {
        ApplicantCreation.ResponseDto responseDto = applicantService.addApplicant(recruitingId, username);

        // TODO: 2022-08-18 팀원 등록도 추가 예정

        return new ResponseEntity<>(ApiResult.success(responseDto), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<ApiResult<Void>> applicantRemove (@PathVariable @NotNull Long recruitingId,
                                                            @CurrentUsername String username) {
        applicantService.removeApplicant(recruitingId, username);

        ApiResult<Void> apiResult = ApiResult.<Void>success().build();
        return ResponseEntity.ok(apiResult);
    }

}
