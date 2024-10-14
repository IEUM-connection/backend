package com.springboot.memberHistory;

import java.time.LocalDateTime;

public class MemberHistoryDto {


    private Long memberId;
    private String memberCode;
    private String name;
    private String address;
    private String detailedAddress;
    private String postalCode;
    private String tel;
    private String phone;
    private int age;
    private String notes;
    private String relationship;
    private String medicalHistory;
    private String memberStatus;
    private String adminNote;
    private String latitude;
    private String longitude;
    private boolean milkDeliveryRequest;
    private String documentAttachment;
    private LocalDateTime createdAt;
    // FCM 토큰 코드 추가
    private String fcmToken;
    private Long guardianId;
    private String guardianName;  // 보호자의 이름
    private String guardianPhone; // 보호자의 전화번호
    private String adminName;
    // 휴대폰 미사용 시간
    private int phoneInactiveTimeMs;
    private LocalDateTime modifiedAt;
}
