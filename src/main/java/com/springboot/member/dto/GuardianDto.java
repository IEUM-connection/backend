package com.springboot.member.dto;

import com.springboot.member.entity.Guardian;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

public class GuardianDto {

    @Getter
    @Setter
    public static class Post {
        private String email;
        private String password;
        private String name;
        private String tel;
        private String phone;
        private String address;
        private String detailedAddress;
        private String postalCode;
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
        private String status;
    }

    @Getter
    @Setter
    public static class Response {
        private Long guardianId;
        private String email;
        private String name;
        private String tel;
        private String address;
    }


}
