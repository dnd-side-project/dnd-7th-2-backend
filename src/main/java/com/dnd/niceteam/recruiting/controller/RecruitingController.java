package com.dnd.niceteam.recruiting.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.dto.RecruitingFind;
import com.dnd.niceteam.recruiting.dto.RecruitingModify;
import com.dnd.niceteam.recruiting.service.RecruitingService;
import com.dnd.niceteam.security.CurrentUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RequestMapping("/recruiting")
@RestController
public class RecruitingController {
    private final RecruitingService recruitingService;

    @PostMapping
    public ResponseEntity<ApiResult<RecruitingCreation.ResponseDto>> recruitingAdd (@RequestBody @Valid RecruitingCreation.RequestDto recruitingReqDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        RecruitingCreation.ResponseDto responseDto = recruitingService.addProjectAndRecruiting(username, recruitingReqDto);

        ApiResult<RecruitingCreation.ResponseDto> apiResult = ApiResult.success(responseDto);
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

    // 모집글 상세
    @GetMapping("/{recruitingId}")
    public ResponseEntity<ApiResult<RecruitingFind.DetailResponseDto>> recruitingDetails (@PathVariable @NotNull Long recruitingId,
                                                                                          @CurrentUsername String username) {
        RecruitingFind.DetailResponseDto responseDto = recruitingService.getRecruiting(recruitingId, username);

        ApiResult<RecruitingFind.DetailResponseDto> apiResult = ApiResult.success(responseDto);
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

    // 내가 쓴글
    @GetMapping("/me")
    public ResponseEntity<ApiResult<Pagination<RecruitingFind.ListResponseDto>>> myRecruitingList(
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer perSize,
            @RequestParam RecruitingStatus status) {
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

        Pagination<RecruitingFind.RecommendedListResponseDto> recruitings = recruitingService.getRecommendedRecruitings(page, perSize, username);
        ApiResult<Pagination<RecruitingFind.RecommendedListResponseDto>> apiResult = ApiResult.success(recruitings);
        return ResponseEntity.ok(apiResult);
    }

    // 키워드 검색 & 목록 조회(필터링)
    @GetMapping
    public ResponseEntity<ApiResult<Pagination<RecruitingFind.ListResponseDto>>> searchRecruitingList(
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer perSize,
            @RequestParam(required = false) Field field,        // only used at SIDE
            @RequestParam Type type,
            @RequestParam(required = false) String searchWord,
            @CurrentUsername String username) {
        Pagination<RecruitingFind.ListResponseDto> recruitings = recruitingService.getSearchRecruitings(page, perSize, field, type, searchWord, username);

        ApiResult<Pagination<RecruitingFind.ListResponseDto>> apiResult = ApiResult.success(recruitings);
        return ResponseEntity.ok(apiResult);
    }

    @PutMapping("/{recruitingId}")
    public ResponseEntity<ApiResult<RecruitingModify.ResponseDto>> recruitingModify (@PathVariable @NotNull Long recruitingId,
                                                                                     @RequestBody RecruitingModify.RequestDto requestDto) {
        RecruitingModify.ResponseDto updatedRecruiting = recruitingService.modifyProjectAndRecruiting(recruitingId, requestDto);
        ApiResult<RecruitingModify.ResponseDto> apiResult = ApiResult.success(updatedRecruiting);
        return ResponseEntity.ok(apiResult);
    }

    @DeleteMapping("/{recruitingId}")
    public ResponseEntity<ApiResult<Void>> recruitingRemove (@PathVariable @NotNull Long recruitingId) {
        recruitingService.removeRecruiting(recruitingId);

        ApiResult<Void> apiResult = ApiResult.<Void>success().build();
        return ResponseEntity.ok(apiResult);
    }
}
