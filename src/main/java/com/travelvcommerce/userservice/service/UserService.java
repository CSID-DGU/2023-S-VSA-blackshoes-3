package com.travelvcommerce.userservice.service;


import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.dto.SellerDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Transactional
@Service
public interface UserService {
    void updateUser(String userId, UserDto userDto);
    void deleteUser(String userId);
    void updatePassword(String userId, String password);

    void registerUser(UserDto.UserRegisterRequestDto registerRequestDto);
    Map<String, String> login(UserDto.UserLoginRequestDto loginRequestDto);

    Map<String, String> socialLogin(String email);

    UserDto.UserInfoResponseDto getUserInfo(String userId);

}