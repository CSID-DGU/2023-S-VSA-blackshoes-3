package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.entity.Role;
import com.travelvcommerce.userservice.entity.Users;
import com.travelvcommerce.userservice.repository.UsersRepository;
import com.travelvcommerce.userservice.security.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;
    private final JwtTokenProvider tokenProvider;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("CustomOAuth2UserService - loadUser method called");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("Attributes: " + oAuth2User.getAttributes());

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            attributes.putAll(response);
            attributes.remove("response");
        }

        OAuth2UserInfo oAuth2UserInfo = null;	//추가
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(attributes);
        }
        else if(provider.equals("naver")){
            oAuth2UserInfo = new NaverUserInfo(attributes);
        }else if(provider.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(attributes);
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        Role role = Role.USER;

        Users user = usersRepository.findByEmail(email)
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setEmail(email);
                    newUser.setRole(role);
                    newUser.setProvider(provider);
                    newUser.setProviderId(providerId);
                    return usersRepository.save(newUser);
                });

        TokenDto tokenDto = tokenProvider.createTokens(email, user.getRole().getRoleName());

        return new CustomUser(attributes, user, tokenDto, provider);
    }
}
