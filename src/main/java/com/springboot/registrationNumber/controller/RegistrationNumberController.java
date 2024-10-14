package com.springboot.registrationNumber.controller;

// 필요한 클래스 임포트
import com.springboot.registrationNumber.service.RegistrationNumberService;
import com.springboot.registrationNumber.service.CodefRegistrationNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// REST API 요청을 처리하는 컨트롤러 클래스 선언
@RestController
@RequestMapping("/api/identity")  // 기본 URL 경로 설정
public class RegistrationNumberController {

    // CodefRegistrationNumberService 클래스의 인스턴스를 주입받는 변수
    @Autowired
    private CodefRegistrationNumberService authService;

    // RegistrationNumberService 클래스의 인스턴스를 주입받는 변수
    @Autowired
    private RegistrationNumberService verificationService;

    // 신분증 검증을 처리하는 POST 요청 메서드
    @PostMapping("/verify")
    public Map<String, Object> verifyIdentity(
            @RequestParam String name,         // 요청에서 받은 이름
            @RequestParam String phoneNo,      // 요청에서 받은 전화번호
            @RequestParam String identity,      // 요청에서 받은 신분증 정보
            @RequestParam int telecom
    ) {
        // authService를 통해 accessToken을 가져옴
        String accessToken = authService.getAccessToken();
        // verificationService를 사용해 신분증 검증 로직을 실행하고 결과를 반환
        return verificationService.verifyIdentity(accessToken, identity, name, phoneNo, telecom);
    }

    // 추가 신분증 인증을 처리하는 POST 요청 메서드
    @PostMapping("/add-verify")
    public Map<String, Object> completeAuth(
            @RequestParam String name,         // 요청에서 받은 이름
            @RequestParam String phoneNo,      // 요청에서 받은 전화번호
            @RequestParam String identity, // 요청에서 받은 신분증 정보
            @RequestParam int telecom
    ) {
        // authService를 통해 accessToken을 가져옴
        String accessToken = authService.getAccessToken();
        // verificationService를 사용해 추가 인증 로직을 실행하고 결과를 반환
        return verificationService.addVerify(accessToken, identity, name, phoneNo, telecom);
    }
}
