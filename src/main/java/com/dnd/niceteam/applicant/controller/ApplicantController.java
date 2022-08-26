package com.dnd.niceteam.applicant.controller;

import com.dnd.niceteam.applicant.dto.ApplicantCreation;
import com.dnd.niceteam.applicant.dto.ApplicantFind;
import com.dnd.niceteam.applicant.service.ApplicantService;
import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.security.CurrentUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruiting")
public class ApplicantController {
    private final ApplicantService applicantService;

    @PostMapping("/{recruitingId}/applicant")
    public ResponseEntity<ApiResult<ApplicantCreation.ResponseDto>> applicantAdd(@PathVariable @NotNull Long recruitingId,
                                                                                 @CurrentUsername String username) {
        ApplicantCreation.ResponseDto responseDto = applicantService.addApplicant(recruitingId, username);

        return new ResponseEntity<>(ApiResult.success(responseDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{recruitingId}/applicant")
    public ResponseEntity<ApiResult<Void>> applicantRemove(@PathVariable @NotNull Long recruitingId,
                                                           @CurrentUsername String username) {
        applicantService.removeApplicant(recruitingId, username);

        ApiResult<Void> apiResult = ApiResult.<Void>success().build();
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/applicant/me")
    public ResponseEntity<ApiResult<Pagination<ApplicantFind.ListResponseDto>>> myApplyList(@RequestParam(defaultValue = "1", required = false) Integer page,
                                                                                            @RequestParam(defaultValue = "10", required = false) Integer perSize,
                                                                                            @RequestBody ApplicantFind.ListRequestDto requestDto,
                                                                                            @CurrentUsername String username) {
        Pagination<ApplicantFind.ListResponseDto> applications = applicantService.getMyApplicnts(page, perSize, requestDto, username);
        ApiResult<Pagination<ApplicantFind.ListResponseDto>> apiResult = ApiResult.success(applications);
        return ResponseEntity.ok(apiResult);
    }
}