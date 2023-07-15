package com.travelvcommerce.userservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import com.travelvcommerce.userservice.entity.Role;
import com.travelvcommerce.userservice.security.CustomUser;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

//클라이언트와 통신할 때 Dto 사용
@Data
public class UserDto {
    private int id;
    private String userId;
    private String email;
    private String name;
    private String password;
    private LocalDate birthdate;
    private Role role;
    private String provider;
    private String providerId;
    private TokenDto tokenDto;
    private Collection<? extends GrantedAuthority> authorities;

    @Data
    public static class UserLoginRequestDto {
        private String email;
        private String password;
    }

    @Data
    public static class UserRegisterRequestDto {
        private String email;
        private String password;
        private String name;
        private LocalDate birthdate;
        private Role role;
        private String provider;
        private String providerId;
    }
}
