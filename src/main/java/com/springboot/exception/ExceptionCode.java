package com.springboot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ExceptionCode {
    QUESTION_NOT_FOUND(404,"Question Not Found"),

    ANSWER_NOT_FOUND(404,"Answer Not Found");;



    @Getter
    private int statusCode;

    @Getter
    private String statusDescription;
}