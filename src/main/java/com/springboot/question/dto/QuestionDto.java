package com.springboot.question.dto;

import com.springboot.answer.dto.AnswerDto;
import com.springboot.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class QuestionDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post {
        @NotBlank
        private String questionTitle;

        @NotBlank
        private String questionContent;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private long questionId;

        @NotBlank
        private String questionTitle;

        @NotBlank
        private String questionContent;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private long questionId;
        private String questionTitle;
        private String questionContent;
        private LocalDateTime questionDate;
        private Question.QuestionStatus questionStatus;
        private String responseContent;
    }
}
