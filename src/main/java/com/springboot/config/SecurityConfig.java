package com.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService guardianDetailsService;
    private final UserDetailsService adminDetailsService;

    public SecurityConfig(UserDetailsService guardianDetailsService, UserDetailsService adminDetailsService) {
        this.guardianDetailsService = guardianDetailsService;
        this.adminDetailsService = adminDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 보호자 이메일과 비밀번호 인증
                .userDetailsService(guardianDetailsService).passwordEncoder(passwordEncoder())
                // 어드민 코드와 비밀번호 인증
                .and()
                .userDetailsService(adminDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/member/login").permitAll()  // 사용자는 코드로 로그인
                .antMatchers("/guardian/**").hasRole("GUARDIAN")  // 보호자 권한
                .antMatchers("/admin/**").hasRole("ADMIN")  // 어드민 권한
                .anyRequest().authenticated()  // 기타 요청은 인증 필요
                .and()
                .sessionManagement()
                .sessionFixation().newSession()
                .maximumSessions(1);  // 사용자는 한 번만 로그인 허용
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
