package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.entity.User;
import com.travelvcommerce.userservice.repository.UserRepository;
import com.travelvcommerce.userservice.security.*;
import com.travelvcommerce.userservice.userinfo.GoogleUserInfo;
import com.travelvcommerce.userservice.userinfo.KakaoUserInfo;
import com.travelvcommerce.userservice.userinfo.NaverUserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("CustomOAuth2UserService - loadUser method called");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("Attributes: " + oAuth2User.getAttributes());

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        adjustAttributesForNaver(attributes, userRequest.getClientRegistration().getRegistrationId());

        OAuth2UserInfo oAuth2UserInfo = createOAuth2UserInfo(attributes, userRequest.getClientRegistration().getRegistrationId());

        return processOAuth2User(attributes, oAuth2UserInfo);
    }

    private void adjustAttributesForNaver(Map<String, Object> attributes, String registrationId) {
        if (registrationId.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            attributes.putAll(response);
            attributes.remove("response");
        }
    }

    private OAuth2UserInfo createOAuth2UserInfo(Map<String, Object> attributes, String registrationId) {
        switch (registrationId) {
            case "google":
                return new GoogleUserInfo(attributes);
            case "naver":
                return new NaverUserInfo(attributes);
            case "kakao":
                return new KakaoUserInfo(attributes);
            default:
                throw new IllegalArgumentException("Unknown registration id: " + registrationId);
        }
    }

    private OAuth2User processOAuth2User(Map<String, Object> attributes, OAuth2UserInfo oAuth2UserInfo) {
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자 정보가 없습니다. 회원가입을 진행해주세요.", null);
        }
        User user = userOptional.get();
        if(user.getPassword() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자 정보가 없습니다. 회원가입을 진행해주세요.", null);
        }

        TokenDto tokenDto = tokenProvider.createTokens(email, user.getRole().getRoleName(), user.getUserId());

        userRepository.updateProviderId(providerId, email);
        userRepository.updateProvider(provider, email);

        return new CustomUser(attributes, user, tokenDto, provider);
    }
}