package com.springboot.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MemberAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        // 응답에 넣어줄 데이터 > 어드민은 로케이션 , 어드민 네임
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", authentication.getName());
        responseBody.put("loginType", authentication.getAuthorities());// 추가된 정보

        // 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Map을 JSON으로 변환하여 응답 본문에 작성
        objectMapper.writeValue(response.getWriter(), responseBody);

//        AuthenticationException exception;
//        log.error("Authentication failed: " + exception.getMessage());
    }

}
