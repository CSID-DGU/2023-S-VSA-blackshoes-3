package com.travelvcommerce.userservice.service;


import com.travelvcommerce.userservice.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Transactional
@Service
public interface UserService {
    UserDto updateUser(String userId, UserDto.UserUpdateRequestDto userUpdateRequestDto);
    void deleteUser(String userId);
    Map<String, String> updatePassword(String userId, UserDto.UserUpdatePasswordRequestDto userUpdatePasswordRequestDto);

    Map<String, String> findPassword(String email, String password);

    UserDto registerUser(UserDto.UserRegisterRequestDto registerRequestDto);
    Map<String, String> login(UserDto.UserLoginRequestDto loginRequestDto);

    Map<String, String> socialLogin(String email);

    UserDto.UserInfoResponseDto getUserInfo(String userId);

}