package com.springboot.alert.controller;

import com.springboot.alert.entity.Alert;
import com.springboot.alert.service.AlertService;
import com.springboot.alert.dto.AlertResponse;
import com.springboot.alert.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AlertController {

  private final AlertService alertService;

  public AlertController(AlertService alertService) {
    this.alertService = alertService;
  }

  @PostMapping("/send-alert")
  public ResponseEntity<?> sendAlert(@RequestBody Alert alert) {
    try {
      Alert savedAlert = alertService.sendAndSaveAlert(alert);
      return ResponseEntity.ok().body(new AlertResponse(savedAlert.getId(), "알림이 성공적으로 발송되고 저장되었습니다."));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ErrorResponse("알림 발송 또는 저장 중 오류가 발생했습니다: " + e.getMessage()));
    }
  }
}
