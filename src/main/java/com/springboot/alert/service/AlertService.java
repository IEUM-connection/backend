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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    logger.info("Sending FCM messages to tokens: {}", tokens);

    for (String token : tokens) {
      if (token != null && !token.isEmpty()) {
        FcmMessageDto fcmMessageDto = new FcmMessageDto();
        fcmMessageDto.setToken(token);
        fcmMessageDto.setTitle(alert.getAlertType());
        fcmMessageDto.setBody(alert.getContent());

        logger.info("Sending FCM message to token: {}", token);

        String result = firebaseCloudMessageService.sendMessage(fcmMessageDto);
        if (result.startsWith("Successfully")) {
          savedAlert.setStatus("SENT");
        } else {
          savedAlert.setStatus("FAILED");
        }

        logger.info("FCM message send result: {}", result);
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

    logger.info("Retrieved Member FCM Tokens: {}", tokens);
    return tokens;
  }

  private List<String> getAdminFcmTokens() {
    List<String> tokens = adminRepository.findAll().stream()
        .map(Admin::getFcmToken)
        .filter(token -> token != null && !token.isEmpty())
        .toList();

    logger.info("Retrieved Admin FCM Tokens: {}", tokens);
    return tokens;
  }

  public Page<Alert> getAllAlerts(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return alertRepository.findAll(pageable);
  }

  @Transactional
  public Alert sendHelpAlert(Alert alert, String adminName) {
    alert.setCreatedAt(LocalDateTime.now());
    alert.setStatus("PENDING");
    alert.setRecipient("관리자");
    alert.setScheduledTime(LocalDateTime.now());

    Alert savedAlert = alertRepository.save(alert);

    Optional<Admin> adminOptional = adminRepository.findByName(adminName);
    if (adminOptional.isPresent()) {
      Admin admin = adminOptional.get();
      String adminToken = admin.getFcmToken();

      if (adminToken != null && !adminToken.isEmpty()) {
        FcmMessageDto fcmMessageDto = new FcmMessageDto();
        fcmMessageDto.setToken(adminToken);
        fcmMessageDto.setTitle("도움 요청 알림");
        fcmMessageDto.setBody(alert.getContent());

        logger.info("Sending help request FCM message to admin: {}", adminName);

        String result = firebaseCloudMessageService.sendMessage(fcmMessageDto);
        if (result.startsWith("Successfully")) {
          savedAlert.setStatus("SENT");
        } else {
          savedAlert.setStatus("FAILED");
        }

        logger.info("Help request FCM message send result: {}", result);
      } else {
        logger.warn("Admin {} has no FCM token", adminName);
        savedAlert.setStatus("FAILED");
      }
    } else {
      logger.warn("Admin not found: {}", adminName);
      savedAlert.setStatus("FAILED");
    }

    return alertRepository.save(savedAlert);
  }

  /**
   * 알림 타입별로 알림을 조회하는 메서드
   *
   * @param type 알림 타입
   * @param page 페이지 번호
   * @param size 페이지 크기
   * @return 해당 타입의 알림 페이지
   */
  public Page<Alert> getAlertsByType(String type, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return alertRepository.findByAlertType(type, pageable);
  }

  /**
   * 지정된 여러 알림 타입으로 알림을 조회하는 메서드
   *
   * @param types 알림 타입 목록
   * @param page  페이지 번호
   * @param size  페이지 크기
   * @return 해당 타입들의 알림 페이지
   */
  public Page<Alert> getAlertsByTypes(List<String> types, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return alertRepository.findByAlertTypeIn(types, pageable);
  }

  /**
   * 지정된 여러 알림 타입을 제외하고 알림을 조회하는 메서드
   *
   * @param types 알림 타입 목록
   * @param page  페이지 번호
   * @param size  페이지 크기
   * @return 제외된 타입을 제외한 알림 페이지
   */
  public Page<Alert> getAlertsExcludingTypes(List<String> types, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return alertRepository.findByAlertTypeNotIn(types, pageable);
  }
}
