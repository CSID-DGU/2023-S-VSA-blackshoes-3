package com.travelvcommerce.userservice.service;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
@Service
public class NaverLoginService implements SocialLoginService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectURI;

    @Override
    public String getAccessToken(String code) {
        String reqURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id="
                + clientId
                + "&client_secret="
                + clientSecret
                + "&code="
                + code
                + "&state=test";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(reqURL, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // 정상적으로 통신이 이루어졌을 경우
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(responseEntity.getBody());
                String accessToken = jsonObject.getString("access_token");
                return accessToken;

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String> getSocialUserInfo(String accessToken) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        String reqURL = "https://openapi.naver.com/v1/nid/me";
        ResponseEntity<String> responseEntity = restTemplate.exchange(reqURL, HttpMethod.GET, entity, String.class);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseEntity.getBody());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JSONObject response = jsonObject.getJSONObject("response");

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", response.getString("id"));
        userInfo.put("email", response.getString("email"));

        return userInfo;
    }
}
