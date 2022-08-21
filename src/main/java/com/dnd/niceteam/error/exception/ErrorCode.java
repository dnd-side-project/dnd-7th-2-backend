package com.dnd.niceteam.error.exception;

import com.dnd.niceteam.common.Domain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.dnd.niceteam.common.Domain.*;
import static javax.servlet.http.HttpServletResponse.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    HANDLE_INTERNAL_SERVER_ERROR(COMMON, SC_INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    HANDLE_ACCESS_DENIED(COMMON, SC_FORBIDDEN, "접근이 허용되지 않습니다."),
    INVALID_INPUT_VALUE(COMMON, SC_BAD_REQUEST, "유효하지 않은 입력 값입니다."),
    INVALID_TYPE_VALUE(COMMON, SC_BAD_REQUEST,  "유효하지 않은 타입입니다."),

    HANDLE_UNAUTHORIZED(AUTH, SC_UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_TOKEN(AUTH, SC_BAD_REQUEST, "유효하지 않은 토큰입니다."),
    USERNAME_NOT_FOUND(AUTH, SC_NOT_FOUND, "존재하지 않는 아이디 입니다."),
    REFRESH_TOKEN_IS_NULL_ERROR(AUTH, SC_UNAUTHORIZED, "로그아웃 상태입니다."),

    MEMBER_NOT_FOUND(MEMBER, SC_NOT_FOUND, "존재하지 않는 회원입니다."),
    DUPLICATE_EMAIL(MEMBER, SC_BAD_REQUEST, "존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(MEMBER, SC_BAD_REQUEST, "존재하는 닉네임입니다."),

    UNIVERSITY_NOT_FOUND(UNIVERSITY, SC_NOT_FOUND, "존재하지 않는 대학교입니다."),
    INVALID_EMAIL_DOMAIN(UNIVERSITY, SC_BAD_REQUEST, "적절하지 않은 이메일 도메인입니다."),

    EMAIL_AUTH_NOT_FOUND(EMAIL_AUTH, SC_NOT_FOUND, "인증번호 발급을 하지 않은 상태입니다."),
    NOT_AUTHENTICATED_EMAIL(EMAIL_AUTH, SC_BAD_REQUEST, "인증되지 않은 이메일입니다."),

    DEPARTMENT_NOT_FOUND(DEPARTMENT, SC_NOT_FOUND, "존재하지 않는 학과입니다."),

    // Project
    INVALID_PROJECT_SCHEDULE(PROJECT, SC_BAD_REQUEST, "프로젝트 기간이 유효하지 않습니다."),
    PROJECT_NOT_FOUND(PROJECT, SC_NOT_FOUND, "존재하지 않는 프로젝트입니다."),

    // Project Member
    PROJECT_MEMBER_NOT_FOUND(PROJECT_MEMBER, SC_NOT_FOUND, "존재하지 않는 팀원입니다."),
    PROJECT_MEMBER_ALREADY_JOINED(PROJECT_MEMBER, SC_BAD_REQUEST, "이미 합류한 팀원입니다."),
    
    PROJECT_ALREADY_DONE(VOTE_GROUP, SC_BAD_REQUEST, "팀플이 이미 완료되었습니다."),

    // Recruiting
    INVALID_RECRUITING_TYPE(RECRUITING, SC_BAD_REQUEST, "모집글 타입이 유효하지 않습니다."),
    RECRUITING_NOT_FOUND(RECRUITING, SC_NOT_FOUND, "존재하지 않는 모집글입니다."),
    ONLY_RECRUITER_AVAILABLE(RECRUITING, SC_BAD_REQUEST, "모집자에게만 허용된 기능입니다."),

    APPLY_IMPOSSIBLE_RECRUITING(RECRUITING, SC_BAD_REQUEST, "지원 불가능한 모집글입니다."),
    APPLY_CANCEL_IMPOSSIBLE_RECRUITING(RECRUITING, SC_BAD_REQUEST, "지원 취소가 불가능한 모집글입니다."),
    APPLICANT_NOT_FOUND(APPLICANT, SC_NOT_FOUND, "존재하지 않는 지원자입니다."),

    COMMENT_NOT_FOUND(COMMENT, SC_NOT_FOUND, "존재하지 않는 댓글입니다."),

    BOOKMARK_NOT_FOUND(BOOKMARK, SC_NOT_FOUND, "존재하지 않는 북마크입니다."),
    BOOKMARK_EXISTING(BOOKMARK, SC_BAD_REQUEST, "이미 존재하는 북마크입니다."),
    BOOKMARK_NOT_OWNED(BOOKMARK, SC_BAD_REQUEST, "소유하지 않은 북마크입니다."),
    
    APPLY_ALREADY_ACCEPTED(APPLICANT, SC_BAD_REQUEST, "이미 수락된 지원입니다."),
    
    // Vote
    EXPIRED_VOTE_GROUP(VOTE_GROUP, SC_BAD_REQUEST, "내보내기 투표 기간이 만료되었습니다."),
    ALREADY_VOTED(VOTE, SC_BAD_REQUEST, "이미 투표에 참여하셨습니다."),
    NOT_ENOUGH_PROJECT_MEMBER(VOTE, SC_BAD_REQUEST, "투표를 하기 위해서는 팀원 수가 3명 이상이어야 합니다."),
    SELF_VOTE(VOTE, SC_BAD_REQUEST, "자기자신에게는 투표할 수 없습니다."),

    // Firebase Notification
    NOTIFICATION_PUSH_FAILED(NOTIFICATION, SC_INTERNAL_SERVER_ERROR, "푸쉬 알림 전송에 실패했습니다."),
    ;

    private final Domain domain;

    private final int status;

    private final String message;
}
