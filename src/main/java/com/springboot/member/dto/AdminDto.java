package com.springboot.member.dto;

import com.springboot.member.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AdminDto {

    @Getter
    @Setter
    public static class Post {
        private String name;            // 관리자 이름
        private String adminCode;       // 관리자 코드 (유니크)
        private String location;        // 관리자 위치
        private String role;            // 관리자 역할 (예: ROLE_ADMIN)
        private String password;        // 관리자 비밀번호
        private String fcmToken;        // FCM 토큰
    }

    @Getter
    @Setter
    public static class Response {
        private long adminId;
        private String name;// 관리자 ID
        private String adminCode;      // 관리자 코드 (유니크)
        private String location;       // 관리자 위치
        private String role;           // 관리자 역할 (예: ROLE_ADMIN)
        private Admin.AdminStatus adminStatus; // 관리자 상태 (활성, 휴면, 탈퇴)
        private String phone;
        private String fcmToken;  // FCM 토큰

    }


    @Getter
    @AllArgsConstructor
    public static class PatchPassword {

        private String password;

        private String newPassword;


    }
     // FCM 토큰 DTO
     @Getter
     @Setter
     public static class FcmTokenUpdate {
         private String fcmToken;
     }
}
