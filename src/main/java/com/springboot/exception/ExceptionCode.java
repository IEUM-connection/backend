package com.springboot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    QUESTION_NOT_FOUND(404,"Question Not Found"),
    ANSWER_NOT_FOUND(404,"Answer Not Found"),
    GUARDIAN_NOT_FOUND(404, "Guardian not found"),
    MEMBER_NOT_FOUND(404, "Member not found"),
    GUARDIAN_EXISTS(409, "Guardian exists"),
    MEMBER_ALREADY_EXISTS(409, "Guardian exists"),
    COMMENT_NOT_FOUND(404, "Comment not found"),
    EMAIL_NOT_AUTH(409, "이메일 인증 실패."),
    CANNOT_REGISTER_COMMENT(403, "Comment do not register"),
    NOT_YOUR_COMMENT(403, "Not your Comment"),
    INVALID_AUTH_CODE(403, "INVALID_AUTH_CODE"),
    CANNOT_CHANGE_GUARDIAN_STATUS(403, "Guardian Status cannot change" ),
    ADMIN_NOT_FOUND(404, "Admin not found"),
    TOKEN_INVALID(403, "토큰값이 유효하지 않습니다."),
    UNAUTHORIZED_GUARDIAN(401, "권한이 없는 회원입니다."),
    CANNOT_CHANGE_ADMIN_STATUS(403, "Admin Status cannot change"),
    NO_UPDATABLE_FIELDS(403, "NO_UPDATABLE_FIELDS"),
    CONFIRM_PASSWORD_MISMATCH(402,"비밀번호가 일치하지 않습니다." ),
    PASSWORD_WRONG(402,"Password Wrong." );




    @Getter
    private int statusCode;

    @Getter
    private String statusDescription;
}