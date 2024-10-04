package com.springboot.member.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
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

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AdminStatus adminStatus = AdminStatus.ADMIN_ACTIVE;




    public enum AdminStatus {
        ADMIN_ACTIVE("활동중"),
        ADMIN_SLEEP("휴면 상태"),
       ADMIN_QUIT("탈퇴 상태");

        @Getter
        private String status;

        AdminStatus(String status) {
            this.status = status;
        }
    }
}
