package com.springboot.medical;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private int rnum;
        private double distance;
        private double latitude;
        private double longitude;
        // 병원분류명 ex)종합병원, 의원,...
        private String dutyDivName;
        private String dutyAddr;
        private String name;
        private String hpid;
        private String dutyTel1;
        private String dutyFax;
        private Integer startTime;
        private Integer endTime;
        // 운영상태
        private Integer isOpen;
    }
}
