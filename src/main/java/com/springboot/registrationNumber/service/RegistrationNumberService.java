package com.springboot.registrationNumber.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

// 서비스 클래스를 나타내는 어노테이션
@Service
public class RegistrationNumberService {

    // 인증에 필요한 jti와 두 번째 요청에 사용할 타임스탬프를 저장할 변수
    private String jti;
    private long twoWayTimestamp;

    // 첫 번째 Pass 인증 시작 요청
    public Map<String, Object> verifyIdentity(String accessToken, String identity, String name, String phoneNo, int telecom) {
        BufferedReader br = null;
        try {
            String apiUrl = "https://development.codef.io/v1/kr/public/mw/identity-card/check-status";

            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            con.setDoOutput(true);

            // 첫 번째 요청 바디 설정
            String requestBody = String.format(
                    "{\"organization\":\"0002\", \"loginType\":\"6\", \"loginTypeLevel\":\"5\", \"telecom\":\"%d\", \"phoneNo\":\"%s\", \"loginUserName\":\"%s\", \"loginIdentity\":\"%s\", \"identity\":\"%s\", \"userName\":\"%s\", \"issueDate\":\"20240919\"}",
                    telecom, phoneNo, name, identity, identity, name);

            OutputStream os = con.getOutputStream();
            os.write(requestBody.getBytes());
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                throw new RuntimeException("Failed to start auth. Response Code: " + responseCode);
            }

            StringBuilder responseStr = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                responseStr.append(inputLine);
            }

            // URL 인코딩된 응답을 디코딩
            String decodedResponse = URLDecoder.decode(responseStr.toString(), StandardCharsets.UTF_8);

            // 디코딩된 응답을 JSON으로 변환
            JSONObject jsonObject = new JSONObject(decodedResponse);

            // 필요한 값 추출
            JSONObject data = jsonObject.getJSONObject("data");
            jti = data.getString("jti");
            twoWayTimestamp = data.getLong("twoWayTimestamp");

            Map<String, Object> result = new HashMap<>();
            result.put("response", jsonObject.toString());  // 전체 응답 반환
            result.put("jti", jti);  // 추출한 jti 저장
            result.put("twoWayTimestamp", twoWayTimestamp);  // 추출한 twoWayTimestamp 저장

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error during auth start", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    // 에러 처리
                }
            }
        }
    }

    // 두 번째 Pass 추가 인증 요청
    public Map<String, Object> addVerify(String accessToken, String identity, String name, String phoneNo, int telecom) {
        BufferedReader br = null;
        try {
            String apiUrl = "https://development.codef.io/v1/kr/public/mw/identity-card/check-status";

            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            con.setDoOutput(true);

            // 두 번째 요청 바디 설정 (첫 번째 응답에서 저장한 jti와 twoWayTimestamp 사용)
            String requestBody = String.format(
                    "{\"organization\":\"0002\", \"loginType\":\"6\", \"loginTypeLevel\":\"5\", \"telecom\":\"%d\", \"phoneNo\":\"%s\", \"loginUserName\":\"%s\", \"loginIdentity\":\"%s\", \"identity\":\"%s\", \"userName\":\"%s\", \"issueDate\":\"20000101\", \"simpleAuth\":\"1\", \"is2Way\":true, \"twoWayInfo\":{\"jobIndex\":0, \"threadIndex\":0, \"jti\":\"%s\", \"twoWayTimestamp\":%d}}",
                    telecom, phoneNo, name, identity, identity, name, jti, twoWayTimestamp);

            OutputStream os = con.getOutputStream();
            os.write(requestBody.getBytes());
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                throw new RuntimeException("Failed to complete auth. Response Code: " + responseCode);
            }

            StringBuilder responseStr = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                responseStr.append(inputLine);
            }

            // URL 인코딩된 응답을 디코딩
            String decodedResponse = URLDecoder.decode(responseStr.toString(), StandardCharsets.UTF_8);

            // 디코딩된 응답을 JSON으로 변환
            JSONObject jsonObject = new JSONObject(decodedResponse);

            Map<String, Object> result = new HashMap<>();
            result.put("response", jsonObject.toString());  // 전체 응답 반환

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error during auth complete", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    // 에러 처리
                }
            }
        }
    }
}
