package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
    GUARDIAN_NOT_FOUND(404, "Guardian not found"),
    GUARDIAN_EXISTS(409, "Guardian exists"),
    COMMENT_NOT_FOUND(404, "Comment not found"),
    EMAIL_NOT_AUTH(409, "Email not Auth"),
    CANNOT_REGISTER_COMMENT(403, "Comment do not register"),
    NOT_YOUR_COMMENT(403, "Not your Comment"),
    INVALID_AUTH_CODE(403, "INVALID_AUTH_CODE"),
    CANNOT_CHANGE_GUARDIAN_STATUS(403, "Guardian Status cannot change" ),
    ADMIN_NOT_FOUND(404, "Admin not found"),
    CANNOT_CHANGE_ADMIN_STATUS(403, "Admin Status cannot change");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}