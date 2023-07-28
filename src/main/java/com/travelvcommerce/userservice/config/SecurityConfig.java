package com.travelvcommerce.userservice.config;

import com.travelvcommerce.userservice.security.JwtTokenFilter;
import com.travelvcommerce.userservice.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;
import java.util.Map;

//Spring Security 설정을 담당합니다. 이 클래스에서는 JWT 필터를 등록하고, URL에 대한 접근 제어 규칙을 설정합니다.
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user-service/users/join").permitAll()
                .antMatchers("/user-service/users/login").permitAll()
                .antMatchers("/user-service/sellers/join").permitAll()
                .antMatchers("/user-service/sellers/login").permitAll()
                .antMatchers("/user-service/refresh").permitAll()
                .antMatchers("/user-service/mail/*").permitAll()
                .antMatchers("/oauth2/authorization/google", "/oauth2/authorization/naver", "/oauth2/authorization/kakao").permitAll()
                .antMatchers("/user-service/users/{userId}").authenticated()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/*")
                .and()
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                // 로그인 성공 핸들러를 추가합니다.
                .successHandler((request, response, authentication) -> {
                    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

                    // 사용자의 이메일 정보를 가져옵니다.
                    String email = token.getPrincipal().getAttribute("email");

                    response.sendRedirect("/user-service/social-login-success?email=" + email);
                })
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .permitAll()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return (request) -> {
            OAuth2User oAuth2User = delegate.loadUser(request);
            if ("naver".equalsIgnoreCase(request.getClientRegistration().getRegistrationId())) {
                return new DefaultOAuth2User(
                        oAuth2User.getAuthorities(),
                        Collections.singletonMap("email", ((Map)oAuth2User.getAttributes().get("response")).get("email")),
                        "email");
            }
            return oAuth2User;
        };
    }
}
