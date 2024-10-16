package com.springboot.alert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AlertResponse {
  private Long alertId;
  private String message;
  private String alertType;
  private LocalDateTime createdAt;
  private String status;
  private String recipient;
}
