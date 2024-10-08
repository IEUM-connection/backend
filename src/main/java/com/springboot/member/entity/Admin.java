package com.springboot.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ADMIN")
public class Admin{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long adminId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String location;


    @Column(unique = true)
    private String adminCode;



    @Column(nullable = false)
    private String role;

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
