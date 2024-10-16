package com.springboot.member.repository;

import com.springboot.member.entity.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {

    Optional<Guardian> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Guardian> findByGuardianStatus(Guardian.GuardianStatus status);
}
