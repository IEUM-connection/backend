package com.springboot.alert.controller;

import com.springboot.alert.entity.Alert;
import com.springboot.alert.service.AlertService;
import com.springboot.alert.dto.AlertResponse;
import com.springboot.alert.dto.ErrorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AlertController {

  private final AlertService alertService;

  public AlertController(AlertService alertService) {
    this.alertService = alertService;
  }

  // 알림 발송 및 저장
  @PostMapping("/send-alert")
  public ResponseEntity sendAlert(@RequestBody Alert alert) {
    try {
      Alert savedAlert = alertService.sendAndSaveAlert(alert);
      return ResponseEntity.ok().body(new AlertResponse(savedAlert.getId(), "알림이 성공적으로 발송되고 저장되었습니다."));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ErrorResponse("알림 발송 또는 저장 중 오류가 발생했습니다: " + e.getMessage()));
    }
  }

  // 알림 목록 조회 (페이지네이션 적용)
  @GetMapping
  public ResponseEntity<?> getAllAlerts(@RequestParam int page, @RequestParam int size) {
    Page<Alert> alertPage = alertService.getAllAlerts(PageRequest.of(page, size));
    List<AlertResponse> alertResponses = alertPage.getContent().stream()
        .map(alert -> new AlertResponse(alert.getId(), alert.getContent()))
        .collect(Collectors.toList());
    return ResponseEntity.ok().body(alertResponses);
  }
}
