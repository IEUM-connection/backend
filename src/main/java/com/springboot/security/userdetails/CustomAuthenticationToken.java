package com.springboot.security.userdetails;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String loginType;  // 로그인 타입 (예: "guardian", "admin", "member")


    public CustomAuthenticationToken(Object principal, Object credentials, String loginType) {
        super(principal, credentials);  // 부모 클래스의 생성자 호출
        this.loginType = loginType;  // 로그인 타입 설정
    }

    public String getLoginType() {
        return loginType;
    }
}