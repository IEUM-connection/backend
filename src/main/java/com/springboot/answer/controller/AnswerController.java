package com.springboot.answer.controller;

import com.springboot.answer.entity.Answer;
import com.springboot.answer.dto.AnswerDto;
import com.springboot.answer.mapper.AnswerMapper;
import com.springboot.answer.service.AnswerService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.print.DocFlavor;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/answers")
@Slf4j
@RequiredArgsConstructor
public class AnswerController {

    private final static String DEFAULT_ANSWER_URL = "/answers";
    private final AnswerMapper answerMapper;
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity postAnswer(@Valid @RequestBody AnswerDto.Post requestBody) {
        Answer createAnswer = answerService.createAnswer(answerMapper.answerPostDtoToAnswer(requestBody));

        URI location = UriCreator.createUri(DEFAULT_ANSWER_URL, createAnswer.getAnswerId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{answer-id}")
    public ResponseEntity patchAnswer(@PathVariable("answer-id") @Positive long answerId,
                                      @Valid @RequestBody AnswerDto.Patch requestBody) {
        requestBody.setAnswerId(answerId);
        Answer answer = answerService.updateAnswer(answerMapper.answerPatchDtoToAnswer(requestBody));
        return new ResponseEntity(
                new SingleResponseDto<>(answerMapper.answerToAnswerResponseDto(answer)), HttpStatus.OK
        );
    }

    @GetMapping("/{answer-id}")
    public ResponseEntity getAnswer(@PathVariable("answer-id") @Positive long answerId) {
        Answer answer = answerService.findAnswer(answerId);
        return new ResponseEntity(
                new SingleResponseDto<>(answerMapper.answerToAnswerResponseDto(answer)), HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity getAnswers(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size,
                                     @RequestParam String sort) {
        // sort 변수를 "_"를 기준으로 나누어 첫 번째 부분으로 정렬 기준을 설정
        Sort sortAnswer = Sort.by(sort.split("_")[0]).ascending();

        // sort 변수의 두 번째 부분이 "desc"라면 내림차순으로 정렬을 변경
        if (sort.split("_")[1].equalsIgnoreCase("desc")) {
            sortAnswer = sortAnswer.descending();  // 내림차순으로 변경
        }

        Page<Answer> pageAnswer = answerService.findAnswers(page - 1, size, sortAnswer);
        List<Answer> answers = pageAnswer.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(answerMapper.answersToAnswerResponseDtos(answers), pageAnswer), HttpStatus.OK
        );
    }

    @DeleteMapping("/{answer-id}")
    public ResponseEntity deleteAnswer(@PathVariable("answer-id") @Positive long answerId) {
        answerService.deleteAnswer(answerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
