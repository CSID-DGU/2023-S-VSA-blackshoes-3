package com.travelvcommerce.userservice.security;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() { return this.attributes; }

    @Override
    public String getProviderId() {
        if (attributes == null || !attributes.containsKey("id")) {
            throw new RuntimeException("Cannot find 'id' in Kakao API response: " + attributes);
        }
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() { return "kakao"; }

    @Override
    public String getEmail() {
        Map kakaoAccount = (Map) attributes.get("kakao_account");
        Object email = kakaoAccount.get("email");
        return email != null ? email.toString() : null;
    }

    @Override
    public String getName() { return attributes.get("nickname").toString(); }

}
