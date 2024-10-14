package com.springboot.member.repository;

import com.springboot.member.entity.Admin;
import com.springboot.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdminCode(String adminCode);

    Optional<Admin> findByLocation(String location);

    Optional<Admin>findByName(String name);

    Optional<Admin> findByLocationContaining(String location);

    List<Admin> findByRole(String role);
}
