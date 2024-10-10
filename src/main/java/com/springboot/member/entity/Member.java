package com.springboot.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long memberId;

    @Column(unique = true, nullable = false)
    private String memberCode;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String detailedAddress;

    @Column(nullable = true)
    private String postalCode;

    @Column(nullable = true)
    private String tel;

    @Column(nullable = false, unique = true)
    private String phone;

    @OneToOne
    @JoinColumn(name = "guardianId", unique = true)
    private Guardian guardian;

    private String emergencyContact;

    private int age;

    @Column(nullable = true)
    private String notes;

    @Column(nullable = true)
    private String relationship;

    @Column(nullable = true)
    private String medicalHistory;

    @Column(nullable = true)
    private int powerUsage;

    @Column(nullable = true)
    private String latitude;

    @Column(nullable = true)
    private String  longitude;

    @Column(nullable = true)
    private int phoneInactiveDuration;

    @Column(nullable = false)
    private String role;

    @Column(nullable = true)
    private boolean milkDeliveryRequest;

    @Column(nullable = true)
    private String documentAttachment;

    @Column(nullable = true)
    private String residentNumber;

    @Lob
    private String adminNote;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.ACTIVE;// Enum 사용

    @Column(nullable = false)
    private LocalDate birthDate;

//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return java.util.List.of();
//    }

    public enum MemberStatus {
        ACTIVE,      // 활성 상태
        AWAITING_APPROVAL,
        MEMBER_QUIT, // 탈퇴한 회원
        SUSPENDED    // 정지된 회원
    }



}
