package com.springboot.member.repository;

import com.springboot.member.entity.Guardian;
import com.springboot.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByNameContainingAndAgeAndMedicalHistoryContaining(String query, int age, String medicalHistory);
    List<Member> findByStatus(String status);

}

