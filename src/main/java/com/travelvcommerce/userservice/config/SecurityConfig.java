package com.travelvcommerce.userservice.config;

import com.travelvcommerce.userservice.security.JwtTokenFilter;
import com.travelvcommerce.userservice.service.oauth2.CustomOAuth2UserService;
import com.travelvcommerce.userservice.service.oauth2.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.filter.CorsFilter;

//Spring Security 설정을 담당합니다. 이 클래스에서는 JWT 필터를 등록하고, URL에 대한 접근 제어 규칙을 설정합니다.
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/user-service/users/join").permitAll()
                .antMatchers("/user-service/users/login").permitAll()
                .antMatchers("/user-service/users/password").permitAll()
                .antMatchers("/user-service/sellers/join").permitAll()
                .antMatchers("/user-service/sellers/login").permitAll()
                .antMatchers("/user-service/sellers/password").permitAll()
                .antMatchers("/user-service/refresh").permitAll()
                .antMatchers("/user-service/logout").permitAll()
                .antMatchers("/user-service/mail/**").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2SuccessHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        http.addFilterAfter(jwtTokenFilter, CorsFilter.class);
    }
}
