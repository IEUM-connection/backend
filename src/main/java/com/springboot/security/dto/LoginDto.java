package com.springboot.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginDto {
    private String email;
    private String password;
    public String adminCode; //관리자 사번
    private String authCode;  // 일회용 인증 코드
}
