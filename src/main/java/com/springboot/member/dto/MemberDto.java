package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

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
        private Long guardianId;
        private String emergencyContact;
        private int age;
        private String notes;
        private String relationship;
        private String medicalHistory;
        private boolean milkDeliveryRequest;
        private String documentAttachment;
        private String x;
        private String y;
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

    }
}
