package com.travelvcommerce.userservice.service;


import com.travelvcommerce.userservice.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Transactional
@Service
public interface UserService {
    Map<String, String> updateUser(String userId, UserDto userDto);
    void deleteUser(String userId);
    Map<String, String> updatePassword(String userId, String password);

    Map<String, String> registerUser(UserDto.UserRegisterRequestDto registerRequestDto);
    Map<String, String> login(UserDto.UserLoginRequestDto loginRequestDto);

    Map<String, String> socialLogin(String email);

    UserDto.UserInfoResponseDto getUserInfo(String userId);

}