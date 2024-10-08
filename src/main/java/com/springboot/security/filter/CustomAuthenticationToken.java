package com.springboot.security.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String authCode;  // 사용자 인증 코드
    private String role;      // 사용자 역할


    public CustomAuthenticationToken(String authCode) {
        super(null, null);  // 인증 코드만 있으므로 principal과 credentials는 null로 설정
        this.authCode = authCode;
        this.role = "MEMBER";  // 역할을 기본적으로 'MEMBER'로 설정
    }


    public CustomAuthenticationToken(Object principal, Object credentials, String role) {
        super(principal, credentials);  // principal과 credentials는 UsernamePasswordAuthenticationToken의 부모 생성자로 넘김
        this.role = role;
    }


    public String getAuthCode() {
        return authCode;
    }


    public String getRole() {
        return role;
    }
}
