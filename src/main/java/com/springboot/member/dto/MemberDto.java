package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDto {
    @Getter
    @Setter
    public static class Post {
        private String name;
        private String address;
        private String detailedAddress;
        private String postalCode;
        private String latitude;
        private String longitude;
        private String tel;
        private String phone;
        private String emergencyContact;
        private int age;
        private String notes;
        private String relationship;
        private String medicalHistory;
        private boolean milkDeliveryRequest;
        private String documentAttachment;
        // FCM 토큰 코드 추가
        private String fcmToken;
    }

    @Getter
    @Setter
    public static class Patch {
        private String name;
        private String tel;
        private String phone;
        private String address;
        private String detailedAddress;
        private String postalCode;
        private String emergencyContact;
        private String notes;
        private String residentNumber;
        private String medicalHistory;
        private String latitude;
        private String longitude;
        private boolean milkDeliveryRequest;
        private String documentAttachment;
        // FCM 토큰 코드 추가
        private String fcmToken;
    }

    @Getter
    @Setter
    public static class Response {
        private Long memberId;
        private String memberCode;
        private String name;
        private String address;
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
        private String guardianName;  // 보호자의 이름
        private String guardianPhone; // 보호자의 전화번호
    }


    // FCM 토큰 코드 추가
    @Getter
    @Setter
    public static class FcmTokenUpdate {
        private String fcmToken;
    }
}
