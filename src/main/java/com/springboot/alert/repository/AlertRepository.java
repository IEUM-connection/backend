package com.springboot.alert.repository;

import com.springboot.alert.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

  /**
   * 알림 타입별로 알림을 조회하는 메서드
   *
   * @param alertType 알림 타입
   * @param pageable  페이지 정보
   * @return 해당 타입의 알림 페이지
   */
  Page<Alert> findByAlertType(String alertType, Pageable pageable);

  /**
   * 여러 알림 타입을 기준으로 알림을 조회하는 메서드
   *
   * @param alertTypes 알림 타입 목록
   * @param pageable   페이지 정보
   * @return 해당 타입들의 알림 페이지
   */
  Page<Alert> findByAlertTypeIn(List<String> alertTypes, Pageable pageable);

  /**
   * 특정 알림 타입을 제외한 알림을 조회하는 메서드
   *
   * @param alertTypes 제외할 알림 타입 목록
   * @param pageable   페이지 정보
   * @return 제외된 타입을 제외한 알림 페이지
   */
  Page<Alert> findByAlertTypeNotIn(List<String> alertTypes, Pageable pageable);
}
