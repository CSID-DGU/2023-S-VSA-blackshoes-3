package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.entity.Users;
import com.travelvcommerce.userservice.repository.UsersRepository;
import com.travelvcommerce.userservice.security.CustomUser;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;
    private final JwtTokenProvider tokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("CustomOAuth2UserService - loadUser method called");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getName();
        String email = oAuth2User.getAttribute("email");

        Users user = usersRepository.findByEmail(email)
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setEmail(email);
                    newUser.setProvider(provider);
                    newUser.setProviderId(providerId);
                    return usersRepository.save(newUser);
                });

        TokenDto tokenDto = tokenProvider.createTokens(email, user.getRole().getRoleName());

        return new CustomUser(oAuth2User.getAttributes(), user, tokenDto);
    }
}
