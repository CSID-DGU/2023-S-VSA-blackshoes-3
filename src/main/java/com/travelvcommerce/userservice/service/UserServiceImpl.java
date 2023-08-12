package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.SellerDto;
import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.entity.Role;
import com.travelvcommerce.userservice.entity.Seller;
import com.travelvcommerce.userservice.entity.User;
import com.travelvcommerce.userservice.repository.UserRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDto registerUser(UserDto.UserRegisterRequestDto registerRequestDto) {
        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .email(registerRequestDto.getEmail())
                .nickname(registerRequestDto.getNickname())
                .birthdate(registerRequestDto.getBirthdate())
                .role(Role.valueOf("USER"))
                .provider(registerRequestDto.getProvider())
                .providerId(registerRequestDto.getProviderId())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        UserDto userDto = UserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birthdate(user.getBirthdate())
                .role(user.getRole())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return userDto;
    }

    //이메일, 닉네임, 생일 수정
    @Override
    @Transactional
    public UserDto updateUser(String userId, UserDto.UserUpdateRequestDto userUpdateRequestDto) {
        User existingUser = userRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("User not found"));

        existingUser.update(userUpdateRequestDto.getNickname());

        UserDto userDto = UserDto.builder()
                .userId(existingUser.getUserId())
                .email(existingUser.getEmail())
                .nickname(existingUser.getNickname())
                .birthdate(existingUser.getBirthdate())
                .role(existingUser.getRole())
                .provider(existingUser.getProvider())
                .providerId(existingUser.getProviderId())
                .createdAt(existingUser.getCreatedAt())
                .updatedAt(existingUser.getUpdatedAt())
                .build();

        return userDto;
    }


    @Override
    public void deleteUser(String userId) {
        userRepository.deleteByUserId(userId);
    }

    @Override
    public Map<String, String> login(UserDto.UserLoginRequestDto loginRequestDto) {
        // 사용자 검색
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

        // 비밀번호 검증
        boolean passwordMatches = passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid password.");
        }

        // JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createTokens(loginRequestDto.getEmail(), user.getRole().getRoleName(), user.getUserId());

        // 토큰과 UUID 반환
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("userId", user.getUserId());
        responseBody.put("accessToken", tokenDto.getAccessToken());
        responseBody.put("refreshToken", tokenDto.getRefreshToken());

        return responseBody;
    }

    public Map<String, String> socialLogin(String email) {
        // 사용자 검색
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (user.getPassword().equals("")) {
            throw new BadCredentialsException("Invalid password.");
        }

        // JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createTokens(email, user.getRole().getRoleName(), user.getUserId());

        // 토큰만 반환
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", tokenDto.getAccessToken());
        responseBody.put("refreshToken", tokenDto.getRefreshToken());

        return responseBody;
    }

    @Override
    public Map<String, String> updatePassword(String userId, UserDto.UserUpdatePasswordRequestDto updatePasswordRequestDto) {
        User existingUser = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        String oldPassword = updatePasswordRequestDto.getOldPassword();
        String newPassword = updatePasswordRequestDto.getNewPassword();
        String password = existingUser.getPassword();

        if (!passwordEncoder.matches(oldPassword, password)) {
            throw new RuntimeException("기존 비밀번호가 일치하지 않습니다.");
        }
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setUpdatedAt(LocalDateTime.now());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("sellerId", existingUser.getUserId());
        responseBody.put("updatedAt", existingUser.getUpdatedAt().toString());
        return responseBody;
    }

    @Override
    public Map<String, String> findPassword(String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            existingUser.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(existingUser.get());
        }

        UserDto.UserFindPasswordResponseDto responseDto = new UserDto.UserFindPasswordResponseDto();
        responseDto.setUpdatedAt(LocalDateTime.now());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("updatedAt", responseDto.getFormattedUpdatedAt());

        return responseBody;
    }

    @Override
    public UserDto.UserInfoResponseDto getUserInfo(String userId) {
        Optional<User> existingUser = userRepository.findByUserId(userId);

        UserDto.UserInfoResponseDto userInfoResponseDto = new UserDto.UserInfoResponseDto();
        if (existingUser.isPresent()) {
            userInfoResponseDto.setNickname(existingUser.get().getNickname());
            userInfoResponseDto.setEmail(existingUser.get().getEmail());
            userInfoResponseDto.setBirthdate(existingUser.get().getBirthdate());
            userInfoResponseDto.setRole(existingUser.get().getRole());
            userInfoResponseDto.setProvider(existingUser.get().getProvider());
        }

        return userInfoResponseDto;
    }
}

