package com.springboot.question.controller;

import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.question.dto.QuestionDto;
import com.springboot.question.entity.Question;
import com.springboot.question.mapper.QuestionMapper;
import com.springboot.question.service.QuestionService;
import com.springboot.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/questions")
@Slf4j
@RequiredArgsConstructor
public class QuestionController {
    private final static String DEFAULT_QUESTION_URL = "/questions";
    private final QuestionMapper questionMapper;
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity postQuestion(@Valid @RequestBody QuestionDto.Post requestBody,
                                       Authentication authentication) {
        Question createQuestion = questionService.createQuestion(questionMapper.questionPostDtoToQuestion(requestBody), authentication);

        URI location = UriCreator.createUri(DEFAULT_QUESTION_URL, createQuestion.getQuestionId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{question-id}")
    public ResponseEntity patchQuestion(@PathVariable("question-id") @Positive long questionId,
                                        @Valid @RequestBody QuestionDto.Patch requestBody,
                                        Authentication authentication) {
        requestBody.setQuestionId(questionId);
        Question question = questionService.updateQuestion(questionMapper.questionPatchDtoToQuestion(requestBody), authentication);
        return new ResponseEntity(
                new SingleResponseDto<>(questionMapper.questionToQuestionResponseDto(question)), HttpStatus.OK
        );
    }

    @GetMapping("/{question-id}")
    public ResponseEntity getQuestion(@PathVariable("question-id") @Positive long questionId,
                                      Authentication authentication) {
        Question question = questionService.findQuestion(questionId, authentication);
        return new ResponseEntity<>(
                new SingleResponseDto<>(questionMapper.questionToQuestionResponseDto(question)), HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity getQuestions(@Positive @RequestParam int page,
                                       @Positive @RequestParam int size,
                                       @RequestParam String sort,
                                       Authentication authentication) {

        // sort 변수를 "_"를 기준으로 나누어 첫 번째 부분으로 정렬 기준을 설정
        Sort sortQuestion = Sort.by(sort.split("_")[0]).ascending();

        // sort 변수의 두 번째 부분이 "desc"라면 내림차순으로 정렬을 변경
        if (sort.split("_")[1].equalsIgnoreCase("desc")) {
            sortQuestion = sortQuestion.descending();  // 내림차순으로 변경
        }


        Page<Question> pageQuestion = questionService.findQuestions(page - 1, size, sortQuestion, authentication);
        List<Question> questions = pageQuestion.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(questionMapper.questionsToQuestionResponseDtos(questions), pageQuestion), HttpStatus.OK
        );
    }

    @DeleteMapping("/{question-id}")
    public ResponseEntity deleteQuestions(@PathVariable("question-id") @Positive long questionId,
                                          Authentication authentication) {
        questionService.deleteQuestion(questionId, authentication);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
