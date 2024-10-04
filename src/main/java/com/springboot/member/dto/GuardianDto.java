package com.springboot.member.dto;

import com.springboot.member.entity.Guardian;
import lombok.Getter;
import lombok.Setter;

public class GuardianDto {

    @Getter
    @Setter
    public static class Post {
        private String email;
        private String password;
        private String name;
        private String tel;
        private String address;
    }

    @Getter
    @Setter
    public static class Patch {
        private String name;
        private String tel;
        private String address;
    }

    @Getter
    @Setter
    public static class Response {
        private Long guardianId;
        private String email;
        private String name;
        private String tel;
        private String address;

        public Response(Guardian guardian) {
            this.guardianId = guardian.getGuardianId();
            this.email = guardian.getEmail();
            this.name = guardian.getName();
            this.tel = guardian.getTel();
            this.address = guardian.getAddress();
        }
    }
}
