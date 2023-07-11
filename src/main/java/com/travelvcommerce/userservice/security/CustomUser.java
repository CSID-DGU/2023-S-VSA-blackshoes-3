package com.travelvcommerce.userservice.security;

import com.travelvcommerce.userservice.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomUser implements OAuth2User {
    private final OAuth2User oAuth2User;
    private Users user;
    private String token;

    public CustomUser(Map<String, Object> attributes, Users user, String token) {
        this.oAuth2User = new DefaultOAuth2User(
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole()),
                attributes,
                "email");
        this.user = user;
        this.token = token;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public Users getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
