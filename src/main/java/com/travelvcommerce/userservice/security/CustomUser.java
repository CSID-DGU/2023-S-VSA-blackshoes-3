package com.travelvcommerce.userservice.security;

import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.entity.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class CustomUser implements OAuth2User {
    private final OAuth2User oAuth2User;
    private Users user;

    private TokenDto tokenDto;  // 수정: tokenDto 필드 추가

    public CustomUser(Map<String, Object> attributes, Users user, TokenDto tokenDto) { // 수정: 생성자 매개변수 수정
        this.oAuth2User = new DefaultOAuth2User(
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole().getRoleName()),
                attributes,
                "email");
        this.user = user;
        this.tokenDto = tokenDto;  // 수정: tokenDto 필드 설정
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
}
