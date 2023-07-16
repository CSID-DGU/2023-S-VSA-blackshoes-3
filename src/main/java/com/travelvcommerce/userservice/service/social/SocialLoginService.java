package com.travelvcommerce.userservice.service.social;

import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.Map;

public interface SocialLoginService {
    //getAccessToken
    String getAccessToken(String code) throws JSONException;

    //getSocialUserInfo
    Map<String, String> getSocialUserInfo(String accessToken) throws JSONException;

}
