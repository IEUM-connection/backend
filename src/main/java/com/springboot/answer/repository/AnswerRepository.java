package com.springboot.answer.repository;

import com.springboot.answer.entity.Answer;
import com.springboot.member.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Page<Answer> findAllByAdmin(Pageable pageable, Admin admin);
}
