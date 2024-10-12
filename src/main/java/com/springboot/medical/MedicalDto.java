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
        private String dutyDivName;
        private String dutyAddr;
        private String dutyName;
        private String hpid;
        private String dutyTel1;
        private String dutyFax;
        private Integer startTime;
        private Integer endTime;
    }
}
