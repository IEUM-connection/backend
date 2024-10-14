package com.springboot.medical;

import com.springboot.dto.SingleResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


@RestController
public class MedicalController {
    @Value("${medical.apikey}")
    private String apikey;

    private final MedicalMapper medicalMapper;

    public MedicalController(MedicalMapper medicalMapper) {
        this.medicalMapper = medicalMapper;
    }

    @GetMapping("/medical")
    public ResponseEntity mediacl(@RequestParam String location) throws IOException {
        // 경도,위도로 들어옴
        // 근처 약국을 받아오는 api
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyLcinfoInqire"); /*URL*/

        //주소 파싱
        String[] param = location.split(",");

        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apikey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("WGS84_LON","UTF-8") + "=" + URLEncoder.encode(param[0], "UTF-8")); /*위도-Y*/
        urlBuilder.append("&" + URLEncoder.encode("WGS84_LAT","UTF-8") + "=" + URLEncoder.encode(param[1], "UTF-8")); /*경도-X*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("30", "UTF-8")); /*목록 건수*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(convertXML(sb.toString()).toString());

        return ResponseEntity.ok(new SingleResponseDto<>(medicalMapper.infoToResponseDto(convertXML(sb.toString()))));
    }

    @GetMapping("/hospital")
    public ResponseEntity hospital(@RequestParam String location) throws IOException {
        // 경도,위도로 들어옴
        // 근처 병원을 받아오는 api
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncLcinfoInqire"); /*URL*/

        //주소 파싱
        String[] param = location.split(",");

        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + apikey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("WGS84_LAT","UTF-8") + "=" + URLEncoder.encode(param[1], "UTF-8")); /*위도-Y*/
        urlBuilder.append("&" + URLEncoder.encode("WGS84_LON","UTF-8") + "=" + URLEncoder.encode(param[0], "UTF-8")); /*경도-X*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("30", "UTF-8")); /*목록 건수*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(convertXML(sb.toString()).toString());

        return ResponseEntity.ok(new SingleResponseDto<>(medicalMapper.infoToResponseDto(convertXML(sb.toString()))));
    }

    public JSONObject convertXML(String xml) throws JSONException {
        JSONObject json = XML.toJSONObject(xml);

        return json;
    }
}