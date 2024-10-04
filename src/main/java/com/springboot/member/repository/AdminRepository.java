package com.springboot.member.repository;

import com.springboot.member.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminmembername(String membername);

    Optional<Admin> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Admin> findByAdminCode(String adminCode);
}
