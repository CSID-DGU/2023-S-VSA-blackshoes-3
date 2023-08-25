package com.travelvcommerce.userservice.security;

import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CustomUser implements OAuth2User {
    private final OAuth2User oAuth2User;
    private User user;

    private TokenDto tokenDto;  // 수정: tokenDto 필드 추가

    public CustomUser(Map<String, Object> attributes, User user, TokenDto tokenDto, String provider) { // 수정: 생성자 매개변수 수정
        Map<String, Object> attributesWithEmail;

        // provider에 따라 email의 위치가 다를 수 있음
        if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccountAttributes != null) {
                attributesWithEmail = new HashMap<>(attributes);
                attributesWithEmail.put("email", kakaoAccountAttributes.get("email"));
            } else {
                attributesWithEmail = attributes;
            }
        } else {
            attributesWithEmail = attributes;
        }

        this.oAuth2User = new DefaultOAuth2User(
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole().getRoleName()),
                attributesWithEmail,
                "email");
        this.user = user;
        this.tokenDto = tokenDto;
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

    public User getUser() {
        return user;
    }
}
