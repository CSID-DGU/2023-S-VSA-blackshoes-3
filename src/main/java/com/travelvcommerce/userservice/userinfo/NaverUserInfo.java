package com.travelvcommerce.userservice.userinfo;

import com.travelvcommerce.userservice.security.OAuth2UserInfo;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getProviderId() {
        if (attributes == null || !attributes.containsKey("id")) {
            throw new RuntimeException("Cannot find 'id' in Naver API response: " + attributes);
        }
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        Object email = attributes.get("email");
        return email != null ? email.toString() : null;
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
