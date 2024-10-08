package com.springboot.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GUARDIAN")
public class Guardian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guardianId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    private String address;

    private String detailedAddress;

    private String postalCode;

    private String tel;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String role;
//
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
//    }




    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private GuardianStatus guardianStatus = GuardianStatus.GUARDIAN_ACTIVE;



    public enum GuardianStatus {
        GUARDIAN_ACTIVE("활동중"),
        GUARDIAN_SLEEP("휴면 상태"),
        GUARDIAN_QUIT("탈퇴 상태");

        @Getter
        private String status;

        GuardianStatus(String status) {
            this.status = status;
        }
    }


}
