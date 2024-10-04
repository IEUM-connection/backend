package com.springboot.RegistrationNumber.service;

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

    // 첫 번째 Pass 인증 요청을 처리하는 메서드
    public Map<String, Object> verifyIdentity(String accessToken, String identity, String name, String phoneNo) {
        BufferedReader br = null; // 서버로부터 받은 응답을 읽을 BufferedReader 선언
        try {
            // API 요청을 보낼 URL
            String apiUrl = "https://development.codef.io/v1/kr/public/mw/identity-card/check-status";

            // URL 객체 생성
            URL url = new URL(apiUrl);
            // URL에 연결을 열고, POST 방식으로 요청 설정
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            // 요청 헤더에 JSON 형식으로 보낸다고 설정
            con.setRequestProperty("Content-Type", "application/json");
            // 요청 헤더에 인증 토큰을 추가
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            // 데이터를 보낼 수 있도록 Output을 활성화
            con.setDoOutput(true);

            // 첫 번째 요청의 바디(보낼 데이터)를 설정, %s는 나중에 phoneNo, name, identity로 대체
            String requestBody = String.format(
                    "{\"organization\":\"0002\", \"loginType\":\"6\", \"loginTypeLevel\":\"1\", \"phoneNo\":\"%s\", \"loginUserName\":\"%s\", \"loginIdentity\":\"%s\", \"identity\":\"%s\", \"userName\":\"%s\", \"issueDate\":\"20240919\"}",
                    phoneNo, name, identity, identity, name);

            // 서버에 데이터를 보내기 위해 OutputStream을 열고 요청 바디를 작성
            OutputStream os = con.getOutputStream();
            os.write(requestBody.getBytes());  // 요청 데이터를 바이트로 변환해 전송
            os.flush();  // 스트림을 비워서 데이터를 즉시 전송
            os.close();  // 스트림 닫기

            // 서버에서 받은 응답 코드 확인 (200이면 성공)
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답이 성공적일 경우, 데이터를 읽기 위해 BufferedReader 사용
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 실패하면 예외 발생
                throw new RuntimeException("Failed to start auth. Response Code: " + responseCode);
            }

            // 응답 내용을 읽어서 저장
            StringBuilder responseStr = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {  // 줄 단위로 응답 읽기
                responseStr.append(inputLine);  // 응답 내용을 StringBuilder에 추가
            }

            // URL 인코딩된 응답을 디코딩 (문자열을 읽기 쉽게 변환)
            String decodedResponse = URLDecoder.decode(responseStr.toString(), StandardCharsets.UTF_8);

            // 디코딩된 문자열을 JSON 객체로 변환
            JSONObject jsonObject = new JSONObject(decodedResponse);

            // JSON 데이터에서 필요한 값을 추출
            JSONObject data = jsonObject.getJSONObject("data");
            jti = data.getString("jti");  // jti 값을 추출하여 저장
            twoWayTimestamp = data.getLong("twoWayTimestamp");  // twoWayTimestamp 값을 추출하여 저장

            // 응답과 추출한 데이터를 담아 반환할 Map 객체 생성
            Map<String, Object> result = new HashMap<>();
            result.put("response", jsonObject.toString());  // 전체 응답을 넣음
            result.put("jti", jti);  // 추출한 jti 값을 넣음
            result.put("twoWayTimestamp", twoWayTimestamp);  // 추출한 twoWayTimestamp 값을 넣음

            return result;  // 결과 반환

        } catch (Exception e) {
            // 예외가 발생하면 오류 메시지를 포함한 예외 발생
            throw new RuntimeException("Error during auth start", e);
        } finally {
            // BufferedReader가 열려 있으면 닫아줌
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    // 예외가 발생하면 처리
                }
            }
        }
    }

    // 두 번째 Pass 인증 요청을 처리하는 메서드
    public Map<String, Object> addVerify(String accessToken, String identity, String name, String phoneNo) {
        BufferedReader br = null;  // 서버로부터 받은 응답을 읽을 BufferedReader 선언
        try {
            // 두 번째 요청을 보낼 API URL
            String apiUrl = "https://development.codef.io/v1/kr/public/mw/identity-card/check-status";

            // URL 객체 생성
            URL url = new URL(apiUrl);
            // URL에 연결을 열고, POST 방식으로 요청 설정
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            // 요청 헤더에 JSON 형식으로 보낸다고 설정
            con.setRequestProperty("Content-Type", "application/json");
            // 요청 헤더에 인증 토큰을 추가
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            // 데이터를 보낼 수 있도록 Output을 활성화
            con.setDoOutput(true);

            // 두 번째 요청의 바디 설정, 첫 번째 요청에서 얻은 jti와 twoWayTimestamp를 사용
            String requestBody = String.format(
                    "{\"organization\":\"0002\", \"loginType\":\"6\", \"loginTypeLevel\":\"1\", \"phoneNo\":\"%s\", \"loginUserName\":\"%s\", \"loginIdentity\":\"%s\", \"identity\":\"%s\", \"userName\":\"%s\", \"issueDate\":\"20000101\", \"simpleAuth\":\"1\", \"is2Way\":true, \"twoWayInfo\":{\"jobIndex\":0, \"threadIndex\":0, \"jti\":\"%s\", \"twoWayTimestamp\":%d}}",
                    phoneNo, name, identity, identity, name, jti, twoWayTimestamp);

            // 서버에 데이터를 보내기 위해 OutputStream을 열고 요청 바디를 작성
            OutputStream os = con.getOutputStream();
            os.write(requestBody.getBytes());  // 요청 데이터를 바이트로 변환해 전송
            os.flush();  // 스트림을 비워서 데이터를 즉시 전송
            os.close();  // 스트림 닫기

            // 서버에서 받은 응답 코드 확인 (200이면 성공)
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답이 성공적일 경우, 데이터를 읽기 위해 BufferedReader 사용
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 실패하면 예외 발생
                throw new RuntimeException("Failed to complete auth. Response Code: " + responseCode);
            }

            // 응답 내용을 읽어서 저장
            StringBuilder responseStr = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {  // 줄 단위로 응답 읽기
                responseStr.append(inputLine);  // 응답 내용을 StringBuilder에 추가
            }

            // URL 인코딩된 응답을 디코딩 (문자열을 읽기 쉽게 변환)
            String decodedResponse = URLDecoder.decode(responseStr.toString(), StandardCharsets.UTF_8);

            // 디코딩된 문자열을 JSON 객체로 변환
            JSONObject jsonObject = new JSONObject(decodedResponse);

            // 응답을 담아 반환할 Map 객체 생성
            Map<String, Object> result = new HashMap<>();
            result.put("response", jsonObject.toString());  // 전체 응답을 넣음

            return result;  // 결과 반환

        } catch (Exception e) {
            // 예외가 발생하면 오류 메시지를 포함한 예외 발생
            throw new RuntimeException("Error during auth complete", e);
        } finally {
            // BufferedReader가 열려 있으면 닫아줌
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    // 예외가 발생하면 처리
                }
            }
        }
    }
}
