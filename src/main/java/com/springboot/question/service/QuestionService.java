package com.springboot.question.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Guardian;
import com.springboot.member.repository.GuardianRepository;
import com.springboot.question.entity.Question;
import com.springboot.question.repository.QuestionRepository;
import com.springboot.utils.ExtractGuardianEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService extends ExtractGuardianEmail {
    private final QuestionRepository questionRepository;
    private final GuardianRepository guardianRepository;

    public Question createQuestion(Question question, Authentication authentication) {
        Guardian guardian = extractGuardianFromAuthentication(authentication, guardianRepository);
        question.setGuardian(guardian);
        return questionRepository.save(question);
    }

    public Question updateQuestion(Question question, Authentication authentication) {
        Guardian guardian = extractGuardianFromAuthentication(authentication, guardianRepository);
        Question findQuestion = findVerifiedQuestion(question.getQuestionId());

        if (!Objects.equals(findQuestion.getGuardian(), guardian)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_GUARDIAN);
        }
        question.setGuardian(guardian);

        Optional.ofNullable(question.getQuestionTitle())
                .ifPresent(findQuestion::setQuestionTitle);
        Optional.ofNullable(question.getQuestionContent())
                .ifPresent(findQuestion::setQuestionContent);

        findQuestion.setModifiedAt(LocalDateTime.now());

        return questionRepository.save(findQuestion);
    }

    public Question findQuestion(long questionId, Authentication authentication) {
        Guardian guardian = extractGuardianFromAuthentication(authentication, guardianRepository);
        Question question = findVerifiedQuestion(questionId);

        if (!Objects.equals(question.getGuardian(), guardian)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_GUARDIAN);
        }

        return question;
    }


    public Page<Question> findQuestions(int page, int size, Sort sort, Authentication authentication) {
        Guardian guardian = extractGuardianFromAuthentication(authentication, guardianRepository);
        return questionRepository.findAllByGuardian(PageRequest.of(page, size, sort), guardian);
    }

    public void deleteQuestion(long questionId, Authentication authentication) {
        Guardian guardian = extractGuardianFromAuthentication(authentication, guardianRepository);
        Question findQuestion = findVerifiedQuestion(questionId);
        if (findQuestion.getGuardian() != guardian) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_GUARDIAN);
        }
        questionRepository.delete(findQuestion);
    }

    public Question findVerifiedQuestion(long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question findQuestion = optionalQuestion.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
        return findQuestion;
    }
}
