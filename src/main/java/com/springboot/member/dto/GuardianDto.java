package com.springboot.member.dto;

import com.springboot.member.entity.Guardian;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;

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



    @Getter
    @AllArgsConstructor
    public static class PatchPassword{

        private long guardianId;
        private String password;

        public void setGuardianId(long guardianId) {
            this.guardianId = guardianId;
        }
        // wntjr

    }


    @Getter
    @AllArgsConstructor
    public static class Check{ private boolean isAvailable;}




    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailCheckDto {
        @Email
        private String email;
    }
}
