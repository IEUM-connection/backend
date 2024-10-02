package com.springboot.member.repository;

import com.springboot.member.entity.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository<Member> extends JpaRepository<Member, Long> {
}
