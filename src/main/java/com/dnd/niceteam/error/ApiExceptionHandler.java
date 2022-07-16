package com.dnd.niceteam.error;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.error.dto.ErrorResponseDto;
import com.dnd.niceteam.error.exception.BusinessException;
import com.dnd.niceteam.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Objects.isNull;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResult<Void>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponseDto response = ErrorResponseDto.of(errorCode);
        ApiResult<Void> result = ApiResult.<Void>builder()
                .status(ApiResult.Status.FAIL)
                .error(response)
                .build();
        return new ResponseEntity<>(result, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResult<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        ErrorResponseDto response = ErrorResponseDto.of(ex);
        ApiResult<Void> result = ApiResult.<Void>builder()
                .status(ApiResult.Status.FAIL)
                .error(response)
                .build();
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiResult<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.HANDLE_ACCESS_DENIED;
        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        String message = errorCode.getMessage();
        ErrorResponseDto response = new ErrorResponseDto(errorCode.getCode(), message);
        ApiResult<Void> result = ApiResult.<Void>builder()
                .status(ApiResult.Status.FAIL)
                .error(response)
                .build();
        return new ResponseEntity<>(result, status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResult<Void>> handleException(Exception ex) {
        ErrorCode errorCode = ErrorCode.HANDLE_INTERNAL_SERVER_ERROR;
        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        String message = errorCode.getMessage();
        ErrorResponseDto response = new ErrorResponseDto(errorCode.getCode(), message);
        ApiResult<Void> result = ApiResult.<Void>builder()
                .status(ApiResult.Status.ERROR)
                .error(response)
                .build();
        return new ResponseEntity<>(result, status);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponseDto response = ErrorResponseDto.of(errorCode, ex.getBindingResult());
        ApiResult<Void> result = ApiResult.<Void>builder()
                .status(ApiResult.Status.FAIL)
                .error(response)
                .build();
        return handleExceptionInternal(ex, result, headers, HttpStatus.valueOf(errorCode.getStatus()), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        body = isNull(body) ? ApiResult.<Void>builder().status(ApiResult.Status.ERROR).build() : body;
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
