package com.springboot.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class GuardianDto {

    @Getter
    @Setter
    public static class Post {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;

        @NotBlank
        private String name;

        private String tel;

        @NotBlank
        private String phone;
        private String address;
        private String detailedAddress;
        private String postalCode;
        private LocalDate birthDate;

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
        private String phone;
        private String address;
        private LocalDate createdAt;
        private String detailedAddress;
        private String postalCode;
        private LocalDate birthDate;

    }

    @Getter
    @AllArgsConstructor
    public static class PatchPassword {

        private String password;

        private String newPassword;


    }

    @Getter
    @AllArgsConstructor
    public static class Check {
        private boolean isAvailable;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailCheckDto {
        @Email
        private String email;
    }
}
