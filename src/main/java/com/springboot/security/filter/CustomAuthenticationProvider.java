package com.springboot.security.filter;

import com.springboot.security.userdetails.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;  // 사용자 인증 관련 커스텀 서비스
    private final PasswordEncoder passwordEncoder;  // 비밀번호 인코더 (BCrypt 등)


    public CustomAuthenticationProvider(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationToken customToken = (CustomAuthenticationToken) authentication;  // 전달받은 인증 토큰을 커스텀 토큰으로 캐스팅
        String username = customToken.getName();  // 사용자 이름(아이디) 가져옴
        String role = customToken.getRole();  // 사용자 역할(권한) 가져옴

        // role과 username을 기반으로 사용자 정보 로드
        UserDetails userDetails = customUserDetailsService.loadUserByUsernameAndRole(username, role);  // 역할과 이름으로 사용자 정보 조회


        if ("member".equalsIgnoreCase(role)) {
            // member의 경우 비밀번호 검증을 하지 않음
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());  // 비밀번호 없이 인증
        } else {
            // guardian, admin의 경우 비밀번호 검증
            if (!passwordEncoder.matches(customToken.getCredentials().toString(), userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");  // 비밀번호가 일치하지 않을 경우 예외 발생
            }
        }

        // 인증 성공 시 UsernamePasswordAuthenticationToken 반환 (비밀번호와 권한 정보 포함)
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);  // 이 AuthenticationProvider가 CustomAuthenticationToken을 지원하는지 확인
    }
}

