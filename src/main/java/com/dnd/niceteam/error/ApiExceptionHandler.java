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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Objects.isNull;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum 으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResult<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        log.error("handleMethodArgumentTypeMismatchException", ex);
        ErrorCode errorCode = ErrorCode.INVALID_TYPE_VALUE;
        ErrorResponseDto response = ErrorResponseDto.of(ex);
        ApiResult<Void> result = ApiResult.<Void>builder()
                .success(false)
                .error(response)
                .build();
        return ResponseEntity
                .status(HttpStatus.valueOf(errorCode.getStatus()))
                .body(result);
    }

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleMethodArgumentNotValid", ex);
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponseDto response = ErrorResponseDto.of(errorCode, ex.getBindingResult());
        ApiResult<Void> result = ApiResult.<Void>builder()
                .success(false)
                .error(response)
                .build();
        return ResponseEntity
                .status(HttpStatus.valueOf(errorCode.getStatus()))
                .body(result);
    }

    /**
     * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleBindException", ex);
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponseDto response = ErrorResponseDto.of(errorCode, ex.getBindingResult());
        ApiResult<Void> result = ApiResult.<Void>builder()
                .success(false)
                .error(response)
                .build();
        return ResponseEntity
                .status(HttpStatus.valueOf(errorCode.getStatus()))
                .body(result);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiResult<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("handleAccessDeniedException", ex);
        ErrorCode errorCode = ErrorCode.HANDLE_ACCESS_DENIED;
        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        String message = errorCode.getMessage();
        ErrorResponseDto response = new ErrorResponseDto(errorCode.name(), message);
        ApiResult<Void> result = ApiResult.<Void>builder()
                .success(false)
                .error(response)
                .build();
        return ResponseEntity.status(status).body(result);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResult<Void>> handleBusinessException(BusinessException ex) {
        log.error("handleBusinessException", ex);
        ErrorCode errorCode = ex.getErrorCode();
        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        ErrorResponseDto response = ErrorResponseDto.of(errorCode);
        ApiResult<Void> result = ApiResult.<Void>builder()
                .success(false)
                .error(response)
                .build();
        return ResponseEntity.status(status).body(result);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResult<Void>> handleException(Exception ex) {
        log.error("handleException", ex);
        ErrorCode errorCode = ErrorCode.HANDLE_INTERNAL_SERVER_ERROR;
        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        String message = errorCode.getMessage();
        ErrorResponseDto response = new ErrorResponseDto(errorCode.name(), message);
        ApiResult<Void> result = ApiResult.<Void>builder()
                .success(false)
                .error(response)
                .build();
        return ResponseEntity.status(status).body(result);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleExceptionInternal", ex);
        body = isNull(body) ? ApiResult.<Void>builder().success(false).build() : body;
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
