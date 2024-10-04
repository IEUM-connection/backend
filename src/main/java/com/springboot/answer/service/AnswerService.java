package com.springboot.answer.service;

import com.springboot.answer.dto.AnswerDto;
import com.springboot.answer.entity.Answer;
import com.springboot.answer.repository.AnswerRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.question.entity.Question;
import com.springboot.question.repository.QuestionRepository;
import com.springboot.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public Answer createAnswer(Answer answer) {
        // 1. Question 엔티티 조회
        Question question = questionRepository.findById(answer.getQuestion().getQuestionId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));

        // 2. Answer 엔티티 생성 및 설정
        Answer answer2 = new Answer();
        answer.setResponseContent(answer.getResponseContent());
        answer.setQuestion(question);

        // 3. Answer 저장
        Answer savedAnswer = answerRepository.save(answer);

        // 4. responseContent가 null이 아니면 Question 상태 업데이트
        if (answer.getResponseContent() != null && !answer.getResponseContent().isEmpty()) {
            question.setQuestionStatus(Question.QuestionStatus.ANSWERED);
            questionRepository.save(question);
        }

        return savedAnswer;
    }

    public Answer updateAnswer(Answer answer) {
        Answer findAnswer = findVerifiedAnswer(answer.getAnswerId());

        Optional.ofNullable(answer.getResponseContent())
                .ifPresent(findAnswer::setResponseContent);

        findAnswer.setModifiedAt(LocalDateTime.now());

        return answerRepository.save(findAnswer);
    }

    public Answer findAnswer(long answerId) {
        return findVerifiedAnswer(answerId);
    }

    public Page<Answer> findAnswers(int page, int size, Sort sort) {
        return answerRepository.findAll(PageRequest.of(page, size));
    }

    public void deleteAnswer(long answerId) {
        Answer findAnswer = findVerifiedAnswer(answerId);

        // Question과의 관계 해제
        Question question = findAnswer.getQuestion();
        if (question != null) {
            question.setAnswer(null);  // Question에서 Answer 참조를 제거
            question.setQuestionStatus(Question.QuestionStatus.PENDING);  // Question 상태 업데이트
            questionRepository.save(question);  // Question 업데이트
        }

        // Answer 삭제
        answerRepository.delete(findAnswer);
    }


    public Answer findVerifiedAnswer(long answerId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        Answer findAnswer = optionalAnswer.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND));
        return findAnswer;
    }
}
