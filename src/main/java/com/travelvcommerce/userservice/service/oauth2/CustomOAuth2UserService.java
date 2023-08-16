package com.travelvcommerce.userservice.service.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.userservice.entity.User;
import com.travelvcommerce.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    public CustomOAuth2UserService() {
        super();
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            log.info("OAuth2User attributes {} ", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (JsonProcessingException e) {
            log.error("Error while parsing OAuth2User attributes: {}", e.getMessage());
        }
        final String authProvider = userRequest.getClientRegistration().getClientName();
        final String email;

        switch (authProvider.toLowerCase()) {
            case "google":
                log.info("Auth provider: {}", authProvider);
                email = (String) oAuth2User.getAttributes().get("email");
                break;
            case "kakao":
                log.info("Auth provider: {}", authProvider);
                Map<String, String> kakaoAccount = (HashMap<String, String>) oAuth2User.getAttributes().get("kakao_account");
                email = (String) kakaoAccount.get("email");
                break;
            case "naver":
                log.info("Auth provider: {}", authProvider);
                Map<String, String> response = (HashMap<String, String>) oAuth2User.getAttributes().get("response");
                email = (String) response.get("email");
                break;
            default:
                throw new AccessDeniedException("Unsupported auth provider: " + authProvider);
        }

        User user;
        log.info("Trying to pull user info email {} authProvider {} ", email, authProvider);
        try {
            user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        } catch (UsernameNotFoundException e) {
            log.error("User not found with email: {}", email);
            throw e;
        }
        if (user.getProvider() == null || user.getProvider().toLowerCase() != authProvider.toLowerCase()) {
            user.setProvider(authProvider);
        }

        log.info("Successfully pulled user info email {} name {} authProvider {} ", user.getEmail(), user.getNickname(), user.getProvider());
        return new CustomOAuth2User(user.getUserId(), user.getEmail(), oAuth2User.getAttributes());
    }
}

