package com.travelvcommerce.userservice.config;

import com.travelvcommerce.userservice.security.JwtTokenFilter;
import com.travelvcommerce.userservice.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                .antMatchers("/user-service/users/{userId}").authenticated()
                .antMatchers("/oauth2/authorization/google").permitAll()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/google")
                .and()
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .defaultSuccessUrl("/home")
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
}
