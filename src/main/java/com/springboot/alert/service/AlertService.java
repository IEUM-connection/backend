package com.springboot.alert.service;

import com.springboot.alert.entity.Alert;
import com.springboot.alert.repository.AlertRepository;
import com.springboot.member.entity.Admin;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.AdminRepository;
import com.springboot.member.repository.MemberRepository;
import com.springboot.utillity.FirebaseCloudMessageService;
import com.springboot.utillity.FcmMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AlertService {

  private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
  private final AlertRepository alertRepository;
  private final MemberRepository memberRepository;
  private final AdminRepository adminRepository;
  private final FirebaseCloudMessageService firebaseCloudMessageService;

  public AlertService(AlertRepository alertRepository, MemberRepository memberRepository,
                      AdminRepository adminRepository, FirebaseCloudMessageService firebaseCloudMessageService) {
    this.alertRepository = alertRepository;
    this.memberRepository = memberRepository;
    this.adminRepository = adminRepository;
    this.firebaseCloudMessageService = firebaseCloudMessageService;
  }

  @Transactional
  public Alert sendAndSaveAlert(Alert alert) {
    alert.setCreatedAt(LocalDateTime.now());
    alert.setStatus("PENDING");
    Alert savedAlert = alertRepository.save(alert);

    List<String> tokens = new ArrayList<>();
    switch (alert.getRecipient()) {
      case "전체":
        tokens.addAll(getAllFcmTokens());
        break;
      case "관리자":
        tokens.addAll(getAdminFcmTokens());
        break;
      case "대상자":
        tokens.addAll(getMemberFcmTokens());
        break;
      default:
        throw new IllegalArgumentException("Invalid recipient type");
    }

    logger.info("Sending FCM messages to tokens: {}", tokens); // 토큰 목록 확인

    for (String token : tokens) {
      if (token != null && !token.isEmpty()) {
        FcmMessageDto fcmMessageDto = new FcmMessageDto();
        fcmMessageDto.setToken(token);
        fcmMessageDto.setTitle(alert.getAlertType());
        fcmMessageDto.setBody(alert.getContent());

        logger.info("Sending FCM message to token: {}", token); // 각 토큰으로 전송 시 로그

        String result = firebaseCloudMessageService.sendMessage(fcmMessageDto);
        if (result.startsWith("Successfully")) {
          savedAlert.setStatus("SENT");
        } else {
          savedAlert.setStatus("FAILED");
        }

        logger.info("FCM message send result: {}", result); // 전송 결과 로그
      }
    }

    return alertRepository.save(savedAlert);
  }

  private List<String> getAllFcmTokens() {
    List<String> tokens = new ArrayList<>();
    tokens.addAll(getMemberFcmTokens());
    tokens.addAll(getAdminFcmTokens());
    return tokens;
  }

  private List<String> getMemberFcmTokens() {
    List<String> tokens = memberRepository.findAll().stream()
        .map(Member::getFcmToken)
        .filter(token -> token != null && !token.isEmpty())
        .toList();

    logger.info("Retrieved Member FCM Tokens: {}", tokens); // 회원 토큰 확인 로그
    return tokens;
  }

  private List<String> getAdminFcmTokens() {
    List<String> tokens = adminRepository.findAll().stream()
        .map(Admin::getFcmToken)
        .filter(token -> token != null && !token.isEmpty())
        .toList();

    logger.info("Retrieved Admin FCM Tokens: {}", tokens); // 관리자 토큰 확인 로그
    return tokens;
  }

  // 페이지네이션으로 알람 목록 조회하는 메서드 추가
  public Page<Alert> getAllAlerts(PageRequest pageRequest) {
    return alertRepository.findAll(pageRequest);
  }
}
