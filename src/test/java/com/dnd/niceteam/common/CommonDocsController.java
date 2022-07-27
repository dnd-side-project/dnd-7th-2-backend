package com.dnd.niceteam.common;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.error.dto.ErrorResponseDto;
import com.dnd.niceteam.error.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/docs/common")
public class CommonDocsController {

    @GetMapping("/api-result")
    public ResponseEntity<ApiResult<String>> apiResult() {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(
                ErrorCode.INVALID_INPUT_VALUE,
                ErrorResponseDto.FieldError.of("input-field", "input-value", "error-reason"));
        ApiResult<String> result = ApiResult.<String>builder()
                .data("response-data")
                .success(true)
                .error(errorResponseDto)
                .build();
        return ResponseEntity.ok(result);
    }
}
