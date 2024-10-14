package com.springboot.alert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlertResponse {
  private Long alertId;
  private String message;
  private String alertType;
}
