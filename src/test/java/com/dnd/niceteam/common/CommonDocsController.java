package com.dnd.niceteam.common;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.error.dto.ErrorResponseDto;
import com.dnd.niceteam.error.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/docs/common")
public class CommonDocsController {

    @GetMapping("/api-result/success")
    public ResponseEntity<ApiResult<String>> apiResultSuccess() {
        ApiResult<String> result = ApiResult.<String>builder()
                .data("response-data")
                .success(true)
                .build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api-result/error")
    public ResponseEntity<ApiResult<String>> apiResultError() {
        ErrorCode errorCode = ErrorCode.HANDLE_UNAUTHORIZED;
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(errorCode);
        ApiResult<String> result = ApiResult.<String>builder()
                .success(false)
                .error(errorResponseDto)
                .build();
        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        return ResponseEntity.status(status).body(result);
    }

    @GetMapping("/api-result/field-error")
    public ResponseEntity<ApiResult<String>> apiResult() {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(errorCode,
                ErrorResponseDto.FieldError.of("input-field", "input-value", "error-reason"));
        ApiResult<String> result = ApiResult.<String>builder()
                .success(true)
                .error(errorResponseDto)
                .build();
        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        return ResponseEntity.status(status).body(result);
    }
}
