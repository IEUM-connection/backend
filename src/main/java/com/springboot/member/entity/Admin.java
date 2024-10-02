package com.springboot.member.entity;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Collection;

@Entity
public class Admin {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long adminId;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String location;

        @Column(unique = true)
        private String adminCode;

        private String affiliation;

        private Long serviceId;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.List.of();
    }
}
