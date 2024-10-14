package com.springboot.memberHistory;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
public class MemberHistoryDto {

    private Long historyId;

    private Long memberId;  // Primary Key로 자동 증가되는 ID

    private String memberCode;  // 멤버 코드, 유니크하고 필수적


    private String name;  // 멤버 이름, 최대 50자까지 허용


    private String address;  // 주소


    private String detailedAddress;  // 상세 주소


    private String postalCode;  // 우편 번호


    private String tel;  // 전화번호 (일반 전화)


    private String phone;  // 휴대전화 번호, 필수적이고 유니크


    private Long guardianId;  // 보호자 정보, One-to-One 관계


    private String emergencyContact;  // 긴급 연락처


    private int age;  // 나이, 필수


    private String notes;  // 비고(관리자 메모)


    private String relationship;  // 관계 (가족, 친구 등)


    private String medicalHistory;  // 병력

    private int powerUsage;  // 전력 사용량


    private String latitude;  // 위도


    private String longitude;  // 경도


    private int phoneInactiveDuration;  // 휴대전화 비활성화 기간(일수)

    private String role;  // 역할 (예: ROLE_MEMBER, ROLE_ADMIN 등)


    private boolean milkDeliveryRequest;  // 우유 배달 요청 여부


    private String documentAttachment;  // 문서 첨부



    private String adminNote;  // 관리자 코멘트, 큰 텍스트 저장


    private Member.MemberStatus memberStatus ;  // 회원 상태

    private LocalDate birthDate;  // 생년월일

    private LocalDateTime createdAt ;


    private String fcmToken;

    private  String adminName;

    private LocalDateTime modifiedAt;
}
