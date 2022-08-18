package com.dnd.niceteam.recruiting.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.code.ProgressStatus;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.dto.RecruitingFind;
import com.dnd.niceteam.recruiting.service.RecruitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

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

    // 모집글 상세
    @GetMapping("/{recruitingId}")
    public ResponseEntity<ApiResult<RecruitingFind.DetailResponseDto>> recruitingDetails (@PathVariable @NotNull Long recruitingId) {
        RecruitingFind.DetailResponseDto responseDto = recruitingService.getRecruiting(recruitingId);

        ApiResult<RecruitingFind.DetailResponseDto> apiResult = ApiResult.success(responseDto);
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

    // 내가 쓴글
    @GetMapping("/me")
    public ResponseEntity<ApiResult<Pagination<RecruitingFind.ListResponseDto>>> myRecruitingList(
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer perSize,
            @RequestParam ProgressStatus status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pagination<RecruitingFind.ListResponseDto> recruitings = recruitingService.getMyRecruitings(page, perSize, status, username);

        ApiResult<Pagination<RecruitingFind.ListResponseDto>> apiResult = ApiResult.success(recruitings);
        return ResponseEntity.ok(apiResult);
    }

    //사이드 모집글 추천
    @GetMapping("/recommend-side")
    public ResponseEntity<ApiResult<Pagination<RecruitingFind.RecommendedListResponseDto>>> recommendedRecruitingList(
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "4", required = false) Integer perSize
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Pagination<RecruitingFind.RecommendedListResponseDto> recruitings = recruitingService.getRecommendedRecruiting(page, perSize, username);
        ApiResult<Pagination<RecruitingFind.RecommendedListResponseDto>> apiResult = ApiResult.success(recruitings);
        return ResponseEntity.ok(apiResult);
    }
}
