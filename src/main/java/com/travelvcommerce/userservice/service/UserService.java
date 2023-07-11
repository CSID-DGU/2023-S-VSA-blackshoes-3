package com.travelvcommerce.userservice.service;


import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.dto.request.LoginRequestDto;
import com.travelvcommerce.userservice.dto.request.RegisterRequestDto;

public interface UserService {
    void updateUser(String userId, UserDto userDto);
    void deleteUser(String userId);
    void findPassword(String userId);
    void registerUser(RegisterRequestDto registerRequestDto);
    String login(LoginRequestDto loginRequestDto);
}