package com.travelvcommerce.userservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import com.travelvcommerce.userservice.security.CustomUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

//클라이언트와 통신할 때 Dto 사용
@Getter
@Setter
public class UserDto {
    private int id;
    private String userId;
    private String email;
    private String name;
    private LocalDate birthdate;
    private String role;
    private String provider;
    private String providerId;
    private String token;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDto create(CustomUser customUser) {
        UserDto userDto = new UserDto();
        userDto.setId(customUser.getUser().getId());
        userDto.setUserId(customUser.getUser().getUserId());
        userDto.setEmail(customUser.getUser().getEmail());
        userDto.setName(customUser.getUser().getName());
        userDto.setBirthdate(customUser.getUser().getBirthdate());
        userDto.setRole(customUser.getUser().getRole());
        userDto.setProvider(customUser.getUser().getProvider());
        userDto.setProviderId(customUser.getUser().getProviderId());
        userDto.setToken(customUser.getToken());
        userDto.setAuthorities(customUser.getAuthorities());
        return userDto;
    }
}