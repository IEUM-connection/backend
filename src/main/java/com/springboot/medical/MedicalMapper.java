package com.springboot.medical;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicalMapper {
    default List<MedicalDto.Response> infoToResponseDto(JSONObject info){
        System.out.println(info);

        List<MedicalDto.Response> responseList = new ArrayList<>();

        JSONObject response = info.getJSONObject("response");
        JSONObject body = response.getJSONObject("body");
        JSONObject items = body.getJSONObject("items");
        JSONArray itemArray = items.getJSONArray("item");

        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject item = itemArray.getJSONObject(i);

            if(item.isNull("yadmNm")){
                int rnum = item.getInt("rnum");
                double distance = item.getDouble("distance");
                double latitude = item.getDouble("latitude");
                double longitude = item.getDouble("longitude");
                String dutyDivName = item.getString("dutyDivName");
                String dutyAddr = item.getString("dutyAddr");
                String dutyName = item.getString("dutyName");
                String hpid = item.getString("hpid");
                String dutyTel1 = item.getString("dutyTel1");
                String dutyFax = item.optString("dutyFax", ""); // Null일 수 있으므로 optString 사용
                Integer startTime = item.getInt("startTime");
                Integer endTime = item.getInt("endTime");

                MedicalDto.Response medical = new MedicalDto.Response(rnum, distance, latitude, longitude, dutyDivName, dutyAddr, dutyName, hpid, dutyTel1, dutyFax, startTime, endTime);
                responseList.add(medical);
            }
            else {
//                int rnum = item.getInt("rnum");
                double distance = item.getDouble("distance") / 1000.0;
                double latitude = item.getDouble("XPos");
                double longitude = item.getDouble("YPos");
                String dutyDivName = item.getString("yadmNm");
                String dutyAddr = item.getString("addr");
                String dutyName = item.getString("clCdNm");
//                String hpid = item.getString("hpid");
                String dutyTel1 = item.getString("telno");
//                String dutyFax = item.optString("dutyFax", ""); // Null일 수 있으므로 optString 사용

//                Integer startTime = item.getInt("startTime");
//                Integer endTime = item.getInt("endTime");

                MedicalDto.Response medical = new MedicalDto.Response(0, distance, latitude, longitude, dutyDivName, dutyAddr, dutyName, "", dutyTel1, "", 0, 0);
                responseList.add(medical);
            }
        }

        return responseList;
    };
}
