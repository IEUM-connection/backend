package com.springboot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ExceptionCode {
    QUESTION_NOT_FOUND(404,"Question Not Found"),

    ANSWER_NOT_FOUND(404,"Answer Not Found"),
    GUARDIAN_NOT_FOUND(404, "Guardian not found"),
    GUARDIAN_EXISTS(409, "Guardian exists"),
    COMMENT_NOT_FOUND(404, "Comment not found"),
    EMAIL_NOT_AUTH(409, "Email not Auth"),
    CANNOT_REGISTER_COMMENT(403, "Comment do not register"),
    NOT_YOUR_COMMENT(403, "Not your Comment"),
    INVALID_AUTH_CODE(403, "INVALID_AUTH_CODE"),
    CANNOT_CHANGE_GUARDIAN_STATUS(403, "Guardian Status cannot change" ),
    ADMIN_NOT_FOUND(404, "Admin not found"),
    TOKEN_INVALID(403, "토큰값이 유효하지 않습니다."),
    UNAUTHORIZED_GUARDIAN(401, "권한이 없는 회원입니다."),
    CANNOT_CHANGE_ADMIN_STATUS(403, "Admin Status cannot change");



    @Getter
    private int statusCode;

    @Getter
    private String statusDescription;
}