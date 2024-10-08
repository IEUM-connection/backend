package com.springboot.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginDto {
    private String email;
    private String password;
    public String adminCode;
    public String memberCode;
}
