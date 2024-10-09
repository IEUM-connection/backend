package com.springboot.answer.service;

import com.springboot.answer.dto.AnswerDto;
import com.springboot.answer.entity.Answer;
import com.springboot.answer.repository.AnswerRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Admin;
import com.springboot.member.repository.AdminRepository;
import com.springboot.question.entity.Question;
import com.springboot.question.repository.QuestionRepository;
import com.springboot.question.service.QuestionService;
import com.springboot.utils.ExtractAdminCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService extends ExtractAdminCode {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public Answer createAnswer(Answer answer, Authentication authentication) {
        // 1. Admin 객체 추출
        Admin admin = extractAdminFromAuthentication(authentication, adminRepository);

        // 2. Question 엔티티 조회
        Question question = questionRepository.findById(answer.getQuestion().getQuestionId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));

        // 3. Answer 엔티티 생성 및 설정
        Answer answerToSave = new Answer();
        answerToSave.setResponseContent(answer.getResponseContent());
        answerToSave.setQuestion(question);
        answerToSave.setAdmin(admin); // admin을 설정

        // 4. Answer 저장
        Answer savedAnswer = answerRepository.save(answerToSave);

        // 5. responseContent가 null이 아니면 Question 상태 업데이트
        if (answer.getResponseContent() != null && !answer.getResponseContent().isEmpty()) {
            question.setQuestionStatus(Question.QuestionStatus.ANSWERED);
            questionRepository.save(question);
        }

        return savedAnswer;
    }


    public Answer updateAnswer(Answer answer, Authentication authentication) {
        Admin admin = extractAdminFromAuthentication(authentication, adminRepository);
        Answer findAnswer = findVerifiedAnswer(answer.getAnswerId());

        if (!Objects.equals(findAnswer.getAdmin(), admin)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_GUARDIAN);
        }
        answer.setAdmin(admin);

        Optional.ofNullable(answer.getResponseContent())
                .ifPresent(findAnswer::setResponseContent);

        findAnswer.setModifiedAt(LocalDateTime.now());

        return answerRepository.save(findAnswer);
    }

    public Answer findAnswer(long answerId) {
        return findVerifiedAnswer(answerId);
    }


    public Page<Answer> findAnswers(int page, int size, Sort sort, Authentication authentication) {
        Admin admin = extractAdminFromAuthentication(authentication, adminRepository);
        return answerRepository.findAllByAdmin(PageRequest.of(page, size, sort), admin);
    }

    public void deleteAnswer(long answerId, Authentication authentication) {
        Admin admin = extractAdminFromAuthentication(authentication, adminRepository);
        Answer findAnswer = findVerifiedAnswer(answerId);

        if (findAnswer.getAdmin() != admin) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_GUARDIAN);
        }

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
