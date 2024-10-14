package com.springboot.memberHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberHistoryRepository extends JpaRepository<MemberHistory, Long> {

    Page<MemberHistory> findByMemberId(Long memberId, Pageable pageable);
}
