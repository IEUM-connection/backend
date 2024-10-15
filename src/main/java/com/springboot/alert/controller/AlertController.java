package com.springboot.alert.controller;

import com.springboot.alert.entity.Alert;
import com.springboot.alert.service.AlertService;
import com.springboot.alert.dto.AlertResponse;
import com.springboot.alert.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/alerts")
public class AlertController {

  private final AlertService alertService;

  @Autowired
  public AlertController(AlertService alertService) {
    this.alertService = alertService;
  }

  @PostMapping("/send-alert")
  public ResponseEntity<?> sendAlert(@RequestBody Alert alert) {
    try {
      Alert savedAlert = alertService.sendAndSaveAlert(alert);
      return ResponseEntity.ok().body(new AlertResponse(
          savedAlert.getId(),
          "알림이 성공적으로 발송되고 저장되었습니다.",
          savedAlert.getAlertType(),
          savedAlert.getCreatedAt(),
          savedAlert.getStatus()
      ));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ErrorResponse(
          "알림 발송 또는 저장 중 오류가 발생했습니다: " + e.getMessage()
      ));
    }
  }

  @GetMapping
  public ResponseEntity<?> getAllAlerts(@RequestParam int page, @RequestParam int size) {
    try {
      Page<Alert> alertPage = alertService.getAllAlerts(page, size);
      List<AlertResponse> alertResponses = alertPage.getContent().stream()
          .map(alert -> new AlertResponse(
              alert.getId(),
              alert.getContent(),
              alert.getAlertType(),
              alert.getCreatedAt(),
              alert.getStatus()
          ))
          .collect(Collectors.toList());
      return ResponseEntity.ok().body(alertResponses);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ErrorResponse(
          "알림 목록 조회 중 오류가 발생했습니다: " + e.getMessage()
      ));
    }
  }

  @PostMapping("/send-help-alert/{adminName}")
  public ResponseEntity<?> sendHelpAlert(@RequestBody Alert alert, @PathVariable String adminName) {
    try {
      Alert savedAlert = alertService.sendHelpAlert(alert, adminName);
      return ResponseEntity.ok().body(new AlertResponse(
              savedAlert.getId(),
              "도움 요청 알림이 성공적으로 발송되었습니다.",
              savedAlert.getAlertType(),
              savedAlert.getCreatedAt(),
              savedAlert.getStatus() // 상태를 추가한 5번째 인수
      ));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(
          "잘못된 요청: " + e.getMessage()
      ));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ErrorResponse(
          "도움 요청 알림 발송 중 오류가 발생했습니다: " + e.getMessage()
      ));
    }
  }

  /**
   * 특정 타입의 알림을 조회할 수 있는 엔드포인트
   *
   * @param type 알림 타입
   * @param page 페이지 번호
   * @param size 페이지 크기
   * @return 해당 타입의 알림 목록
   */
  @GetMapping("/by-type")
  public ResponseEntity<?> getAlertsByType(
      @RequestParam String type,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    List<String> allowedTypes = Arrays.asList("일반", "긴급", "공지", "정기");
    if (!allowedTypes.contains(type)) {
      return ResponseEntity.badRequest().body(new ErrorResponse("허용되지 않은 알림 타입입니다."));
    }

    try {
      Page<Alert> alertPage = alertService.getAlertsByType(type, page, size);
      List<AlertResponse> alertResponses = alertPage.getContent().stream()
          .map(alert -> new AlertResponse(
              alert.getId(),
              alert.getContent(),
              alert.getAlertType(),
              alert.getCreatedAt(),
              alert.getStatus()
          ))
          .collect(Collectors.toList());
      return ResponseEntity.ok().body(alertResponses);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ErrorResponse(
          "알림 목록 조회 중 오류가 발생했습니다: " + e.getMessage()
      ));
    }
  }

  /**
   * 지정된 네 가지 타입의 알림만 조회할 수 있는 엔드포인트
   *
   * @param page 페이지 번호
   * @param size 페이지 크기
   * @return 지정된 타입의 알림 목록
   */
  @GetMapping("/selected-types")
  public ResponseEntity<?> getSelectedTypesAlerts(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    // 조회할 알림 타입 목록
    List<String> selectedTypes = Arrays.asList("일반", "긴급", "공지", "정기");

    try {
      Page<Alert> alertPage = alertService.getAlertsByTypes(selectedTypes, page, size);
      List<AlertResponse> alertResponses = alertPage.getContent().stream()
          .map(alert -> new AlertResponse(
              alert.getId(),
              alert.getContent(),
              alert.getAlertType(),
              alert.getCreatedAt(),
              alert.getStatus()
          ))
          .collect(Collectors.toList());
      return ResponseEntity.ok().body(alertResponses);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ErrorResponse(
          "알림 목록 조회 중 오류가 발생했습니다: " + e.getMessage()
      ));
    }
  }

  /**
   * 지정된 네 가지 타입을 제외한 알림을 조회할 수 있는 엔드포인트
   *
   * @param page 페이지 번호
   * @param size 페이지 크기
   * @return 제외된 타입을 제외한 알림 목록
   */
  @GetMapping("/exclude-types")
  public ResponseEntity<?> getAlertsExcludingTypes(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    // 제외할 알림 타입 목록
    List<String> excludedTypes = Arrays.asList("일반", "긴급", "공지", "정기");

    try {
      Page<Alert> alertPage = alertService.getAlertsExcludingTypes(excludedTypes, page, size);
      List<AlertResponse> alertResponses = alertPage.getContent().stream()
          .map(alert -> new AlertResponse(
              alert.getId(),
              alert.getContent(),
              alert.getAlertType(),
              alert.getCreatedAt(),
              alert.getStatus()
          ))
          .collect(Collectors.toList());
      return ResponseEntity.ok().body(alertResponses);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ErrorResponse(
          "알림 목록 조회 중 오류가 발생했습니다: " + e.getMessage()
      ));
    }
  }

  @GetMapping("/{alertId}")
  public ResponseEntity<?> getAlertById(@PathVariable("alertId") Long id) {
    try {
      // 알림 ID로 조회
      Optional<Alert> alertOptional = alertService.getAlertById(id);

      // 알림이 존재하지 않을 경우 예외 처리
      if (alertOptional.isEmpty()) {
        return ResponseEntity.badRequest().body(new ErrorResponse("해당 ID의 알림이 존재하지 않습니다."));
      }

      // 조회된 알림 정보를 AlertResponse 객체로 변환하여 반환
      Alert alert = alertOptional.get();
      AlertResponse alertResponse = new AlertResponse(
              alert.getId(),
              alert.getContent(),
              alert.getAlertType(),
              alert.getCreatedAt(),
              alert.getStatus()
      );

      return ResponseEntity.ok().body(alertResponse);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse("잘못된 알림 ID입니다: " + e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ErrorResponse("알림 조회 중 오류가 발생했습니다: " + e.getMessage()));
    }
  }
}
