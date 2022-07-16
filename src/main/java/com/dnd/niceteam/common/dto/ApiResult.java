package com.dnd.niceteam.common.dto;

import com.dnd.niceteam.error.dto.ErrorResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {

    private Status status;

    private T data;

    private ErrorResponseDto error;

    public enum Status {
        SUCCESS,    // 응답 성공
        FAIL,       // Client 요청 실패
        ERROR,      // Server 관련 에러
    }

    public static <R> ApiResult.ApiResultBuilder<R> success() {
        return ApiResult.<R>builder()
                .status(Status.SUCCESS);
    }

    public static <R> ApiResult<R> success(R data) {
        return ApiResult.<R>builder()
                .status(Status.SUCCESS)
                .data(data)
                .build();
    }
}
