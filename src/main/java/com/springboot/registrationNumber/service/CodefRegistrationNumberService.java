package com.springboot.registrationNumber.service;

// 필요한 라이브러리 임포트
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

// 서비스 클래스 선언
@Service
public class CodefRegistrationNumberService {

    // application.properties 파일에서 "codef.client-id" 값을 주입받는 변수
    @Value("${codef.client-id}")
    private String clientId;

    // application.properties 파일에서 "codef.client-secret" 값을 주입받는 변수
    @Value("${codef.client-secret}")
    private String clientSecret;

    // HTTP 요청을 보내기 위한 RestTemplate 객체 생성
    private final RestTemplate restTemplate = new RestTemplate();

    // 접근 토큰을 가져오는 메서드
    public String getAccessToken() {
        // 토큰을 요청할 URL 설정
        String tokenUrl = "https://oauth.codef.io/oauth/token";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        // 헤더에 Content-Type을 폼 URL 인코딩으로 설정
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 클라이언트 ID와 비밀 키를 하나의 문자열로 결합
        String auth = clientId + ":" + clientSecret;
        // 결합된 문자열을 Base64로 인코딩
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        // 인코딩된 문자열을 Authorization 헤더에 추가
        headers.set("Authorization", "Basic " + encodedAuth);

        // HTTP 요청 본문에 grant_type 추가 (client_credentials 방식)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        // 헤더와 본문을 포함한 HTTP 요청 객체 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // 디버깅을 위한 요청 정보 출력
        System.out.println("Requesting Token from URL: " + tokenUrl);
        System.out.println("Request Body: " + body);
        System.out.println("Request Headers: " + headers);

        try {
            // RestTemplate을 사용하여 POST 요청 전송 및 응답 수신
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {}
            );

            // 응답 상태와 본문 출력
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            // 응답 본문에서 데이터 추출
            Map<String, Object> responseBody = response.getBody();
            // "access_token"이 존재하면 해당 값을 반환
            if (responseBody != null && responseBody.containsKey("access_token")) {
                return (String) responseBody.get("access_token");
            } else {
                // "access_token"이 없으면 예외 발생
                throw new RuntimeException("Failed to get access token: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            // HTTP 요청 중 오류 발생 시 오류 정보 출력
            System.out.println("Error occurred: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            System.out.println("Request URL: " + tokenUrl);
            System.out.println("Request Body: " + body);
            System.out.println("Request Headers: " + headers);
            // 예외를 다시 던져 상위에서 처리
            throw e;
        }
    }
}
