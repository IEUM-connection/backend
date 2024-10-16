package com.springboot.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;  // Primary Key로 자동 증가되는 ID

    @Column(unique = true, nullable = false, length = 20)
    private String memberCode;  // 멤버 코드, 유니크하고 필수적

    @Column(nullable = true, length = 50)
    private String name;  // 멤버 이름, 최대 50자까지 허용

    @Column(nullable = true, length = 100)
    private String address;  // 주소

    @Column(nullable = true, length = 100)
    private String detailedAddress;  // 상세 주소

    @Column(nullable = true, length = 10)
    private String postalCode;  // 우편 번호

    @Column(nullable = true, length = 20)
    private String tel;  // 전화번호 (일반 전화)

    @Column(nullable = false, unique = true, length = 15)
    private String phone;  // 휴대전화 번호, 필수적이고 유니크

    @OneToOne
    @JoinColumn(name = "guardian_id", unique = true)  // 보호자 정보, One-to-One 관계
    private Guardian guardian;

    @Column(length = 50)
    private String emergencyContact;  // 긴급 연락처

    @Column(nullable = false)
    private int age;  // 나이, 필수

    @Column(length = 255)
    private String notes;  // 비고(관리자 메모)

    @Column(length = 50)
    private String relationship;  // 관계 (가족, 친구 등)

    @Column(length = 255)
    private String medicalHistory;  // 병력

    @Column(nullable = true)
    private int powerUsage;  // 전력 사용량

    @Column(nullable = true, length = 20)
    private String latitude;  // 위도

    @Column(nullable = true, length = 20)
    private String longitude;  // 경도

    @Column(nullable = true)
    private int phoneInactiveDuration;  // 휴대전화 비활성화 기간(일수)

    @Column(nullable = false)
    private String role;  // 역할 (예: ROLE_MEMBER, ROLE_ADMIN 등)

    @Column(nullable = true)
    private Boolean milkDeliveryRequest;  // 우유 배달 요청 여부

    @Column(length = 255)
    private String documentAttachment;  // 문서 첨부


    @Lob
    @Column(nullable = true)
    private String adminNote;  // 관리자 코멘트, 큰 텍스트 저장

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.AWAITING_APPROVAL;  // 회원 상태

    @Column(nullable = false)
    private LocalDate birthDate;  // 생년월일

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();



    @Column(nullable = true, name = "MEMBER_FCMTOKEN")
    private String fcmToken;

    private  String adminName;

    private String adminPhone;

    public enum MemberStatus {
        ACTIVE,  // 활성 상태
        AWAITING_APPROVAL,  // 승인 대기 상태
        MEMBER_QUIT,  // 탈퇴한 회원
        SUSPENDED  // 정지된 회원
    }


    @PrePersist
    public void generateMemberCode() {
        this.memberCode = "MEM" + UUID.randomUUID().toString().substring(0, 3).toUpperCase();
    }
}
