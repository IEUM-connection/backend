package com.springboot.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.member.entity.Member;
import com.springboot.security.dto.LoginDto;
import com.springboot.security.jwt.JwtTokenizer;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        // 요청의 특정 헤더나 경로를 통해 보호자 또는 어드민 로그인 여부를 확인
        String loginType = request.getParameter("loginType"); // 예: 'guardian' 또는 'admin'

        UsernamePasswordAuthenticationToken authenticationToken;

        // 로그인 타입에 따른 분기 처리
        if ("guardian".equals(loginType)) {
            authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(), loginDto.getPassword());
        } else if ("admin".equals(loginType)) {
            authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getAdminCode(), loginDto.getPassword());
        } else {
            throw new AuthenticationException("잘못된 로그인 타입입니다") {};
        }

        return authenticationManager.authenticate(authenticationToken);
    }

    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain,
                                            Authentication authentication) throws ServletException, IOException {
        Member member = (Member) authentication.getPrincipal();
        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member, accessToken);
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authentication);
        if (member.getMemberStatus() == Member.MemberStatus.MEMBER_QUIT) {
            throw new AuthenticationException("탈퇴한 회원입니다") {};
        }
    }

    protected String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberCode", member.getMemberCode());
        claims.put("roles", member.getRole());
        String subject = member.getMemberCode();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject,
                expiration, base64EncodedSecretKey);
        return accessToken;
    }

    protected String delegateRefreshToken(Member member, String accessToken) {

        String subject = member.getMemberCode();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey, accessToken);

        return refreshToken;
    }

}
