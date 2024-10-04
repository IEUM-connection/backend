package com.springboot.answer.dto;

import com.springboot.answer.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AnswerDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post {
        @NotNull
        private long questionId;

        @NotBlank
        private String responseContent;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private long answerId;

        @NotNull
        private long questionId;

        @NotBlank
        private String responseContent;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private long answerId;
        private long questionId;
        private String responseContent;
        private LocalDateTime answerDate;
        private Answer.AnswerStatus answerStatus;
    }
}
