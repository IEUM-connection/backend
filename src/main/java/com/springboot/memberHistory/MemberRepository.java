package com.springboot.memberHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberHistory, Long> {
}
