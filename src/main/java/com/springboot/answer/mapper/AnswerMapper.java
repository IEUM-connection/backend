package com.springboot.answer.mapper;

import com.springboot.answer.dto.AnswerDto;
import com.springboot.answer.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnswerMapper {

    @Mapping(source = "questionId", target = "question.questionId")
    Answer answerPostDtoToAnswer(AnswerDto.Post requestBody);

    @Mapping(source = "questionId", target = "question.questionId")
    Answer answerPatchDtoToAnswer(AnswerDto.Patch requestBody);

    @Mapping(source = "question.questionId", target = "questionId")
    AnswerDto.Response answerToAnswerResponseDto(Answer answer);

    List<AnswerDto.Response> answersToAnswerResponseDtos(List<Answer> answers);
}
