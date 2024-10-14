package com.springboot.memberHistory;

import com.springboot.member.entity.Guardian;
import com.springboot.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


    private String guardianId;  // 보호자 정보, One-to-One 관계

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

    @Column(nullable = false, length = 20)
    private String role;  // 역할 (예: ROLE_MEMBER, ROLE_ADMIN 등)

    @Column(nullable = true)
    private boolean milkDeliveryRequest;  // 우유 배달 요청 여부

    @Column(length = 255)
    private String documentAttachment;  // 문서 첨부

    @Column(nullable = true, length = 13)
    private String residentNumber;  // 주민등록번호 (암호화 가능)

    @Lob
    @Column(nullable = true)
    private String adminNote;  // 관리자 코멘트, 큰 텍스트 저장

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Member.MemberStatus memberStatus;  // 회원 상태

    @Column(nullable = false)
    private LocalDate birthDate;  // 생년월일

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private final LocalDateTime modifiedAt = LocalDateTime.now();

    @Column(nullable = true, name = "MEMBER_FCMTOKEN")
    private String fcmToken;

    private  String adminName;


}
