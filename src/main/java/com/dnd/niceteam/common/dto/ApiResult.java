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

    private boolean success;

    private T data;

    private ErrorResponseDto error;

    public static <R> ApiResult.ApiResultBuilder<R> success() {
        return ApiResult.<R>builder()
                .success(true);
    }

    public static <R> ApiResult<R> success(R data) {
        return ApiResult.<R>builder()
                .success(true)
                .data(data)
                .build();
    }
}
