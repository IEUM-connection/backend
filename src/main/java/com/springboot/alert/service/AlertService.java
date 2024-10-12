package com.springboot.alert.service;

import com.springboot.alert.entity.Alert;
import com.springboot.alert.repository.AlertRepository;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.utillity.FirebaseCloudMessageService;
import com.springboot.utillity.FcmMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

  private final AlertRepository alertRepository;
  private final MemberRepository memberRepository;
  private final FirebaseCloudMessageService firebaseCloudMessageService;

  public AlertService(AlertRepository alertRepository, MemberRepository memberRepository, FirebaseCloudMessageService firebaseCloudMessageService) {
    this.alertRepository = alertRepository;
    this.memberRepository = memberRepository;
    this.firebaseCloudMessageService = firebaseCloudMessageService;
  }

  @Transactional
  public Alert sendAndSaveAlert(Alert alert) {
    alert.setCreatedAt(LocalDateTime.now());
    alert.setStatus("PENDING");
    Alert savedAlert = alertRepository.save(alert);

    List<Member> recipients;
    switch (alert.getRecipient()) {
      case "전체":
        recipients = memberRepository.findAll();
        break;
      case "보호자":
        recipients = memberRepository.findByRole("GUARDIAN");
        break;
      case "대상자":
        recipients = memberRepository.findByRole("MEMBER");
        break;
      default:
        throw new IllegalArgumentException("Invalid recipient type");
    }

    for (Member recipient : recipients) {
      if (recipient.getFcmToken() != null && !recipient.getFcmToken().isEmpty()) {
        FcmMessageDto fcmMessageDto = new FcmMessageDto();
        fcmMessageDto.setToken(recipient.getFcmToken());
        fcmMessageDto.setTitle(alert.getAlertType());
        fcmMessageDto.setBody(alert.getContent());

        String result = firebaseCloudMessageService.sendMessage(fcmMessageDto);
        if (result.startsWith("Successfully")) {
          savedAlert.setStatus("SENT");
        } else {
          savedAlert.setStatus("FAILED");
        }
      }
    }

    return alertRepository.save(savedAlert);
  }

  // 페이지네이션으로 알람 목록 조회하는 메서드 추가
  public Page<Alert> getAllAlerts(PageRequest pageRequest) {
    return alertRepository.findAll(pageRequest);
  }
}
