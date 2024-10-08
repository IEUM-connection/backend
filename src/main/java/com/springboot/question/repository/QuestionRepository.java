package com.springboot.question.repository;

import com.springboot.member.entity.Guardian;
import com.springboot.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAllByGuardian(Pageable pageable, Guardian guardian);

}
