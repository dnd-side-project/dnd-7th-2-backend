package com.dnd.niceteam.error.exception;

import com.dnd.niceteam.common.Domain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.dnd.niceteam.common.Domain.COMMON;
import static com.dnd.niceteam.common.Domain.AUTH;
import static javax.servlet.http.HttpServletResponse.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    HANDLE_INTERNAL_SERVER_ERROR(COMMON, 1, SC_INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    HANDLE_ACCESS_DENIED(COMMON, 2, SC_FORBIDDEN, "접근이 허용되지 않습니다."),
    INVALID_INPUT_VALUE(COMMON, 3, SC_BAD_REQUEST, "유효하지 않은 입력 값입니다."),
    INVALID_TYPE_VALUE(COMMON, 4, SC_BAD_REQUEST,  "유효하지 않은 타입입니다."),

    MEMBER_NOT_FOUND(COMMON, 5, SC_NOT_FOUND, "존재하지 않는 회원입니다."),

    REFRESH_TOKEN_IS_NULL_ERROR(AUTH, 1, SC_NOT_ACCEPTABLE, "로그아웃 상태입니다."),
    INVALID_TOKEN(AUTH, 4, SC_BAD_REQUEST, "유효하지 않은 토큰입니다."),
    ;

    private final Domain domain;

    private final int codeNum;

    private final int status;

    private final String message;

    public String getCode() {
        return String.format("%s-%03d", domain.name(), codeNum);
    }
}
