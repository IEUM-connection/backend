package com.springboot.member.repository;

import com.springboot.member.entity.Guardian;
import com.springboot.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberCode(String authCode);
    List<Member> findByAddressContaining(String location);

    List<Member> findByNameContainingAndAgeAndMedicalHistoryContaining(String query, int age, String medicalHistory);

    List<Member> findByMemberStatus(Member.MemberStatus status);

    Page<Member> findAll(Pageable pageable);

    List<Member> findByRole(String role);
}

