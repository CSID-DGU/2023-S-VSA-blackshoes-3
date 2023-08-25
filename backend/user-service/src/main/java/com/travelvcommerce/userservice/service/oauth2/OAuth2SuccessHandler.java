package com.travelvcommerce.userservice.service.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.entity.Role;
import com.travelvcommerce.userservice.repository.UserRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Service
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Value("${spring.security.oauth2.redirect-uri}")
    private String REDIRECT_URI;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info(authentication.toString());
        CustomOAuth2User userPrincipal = (CustomOAuth2User) authentication.getPrincipal();
        log.info("userPrincipal {}", userPrincipal.getAttributes().toString());
        String userId = userPrincipal.getName();
        String email = userPrincipal.getEmail();
        String userType = Role.USER.getRoleName();

        log.info("userId {}", userId);
        log.info("email {}", email);

        TokenDto tokenDto = jwtTokenProvider.createTokens(email, userType, userId);
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(REDIRECT_URI + "/social-login?userId=" + userId + "&access-token=" + tokenDto.getAccessToken() + "&refresh-token=" + tokenDto.getRefreshToken());
    }
}
