package com.springboot.member.repository;

import com.springboot.member.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdminCode(String adminCode);

    Optional<Admin> findByLocation(String location);


    Optional<Admin> findByLocationContaining(String location);
}
