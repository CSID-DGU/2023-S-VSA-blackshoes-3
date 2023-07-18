package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.entity.Role;
import com.travelvcommerce.userservice.entity.Users;
import com.travelvcommerce.userservice.repository.UsersRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final UsersRepository usersRepository;


    @Override
    public void registerUser(UserDto.UserRegisterRequestDto registerRequestDto) {
        if(usersRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        Users user = new Users();
        user.setEmail(registerRequestDto.getEmail());
        user.setNickname(registerRequestDto.getNickname());
        user.setBirthdate(registerRequestDto.getBirthdate());
        user.setRole(Role.valueOf("USER"));
        user.setProvider(registerRequestDto.getProvider());
        user.setProviderId(registerRequestDto.getProviderId());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));  // 비밀번호 암호화
        usersRepository.save(user);
    }
    @Override
    public void updateUser(String userId, UserDto userDto){
        Optional<Users> existingUser = usersRepository.findByUserId(userId);
        if(existingUser.isPresent()) {
            existingUser.get().setNickname(userDto.getNickname());
            existingUser.get().setPassword(passwordEncoder.encode(userDto.getPassword()));
            existingUser.get().setBirthdate(userDto.getBirthdate());
            usersRepository.save(existingUser.get());
        }
    }


    @Override
    public void deleteUser(String userId) {
        usersRepository.deleteByUserId(userId);
    }
    @Override
    public Map<String, String> login(UserDto.UserLoginRequestDto loginRequestDto) {
        // 사용자 검색
        Users user = usersRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

        // 비밀번호 검증
        boolean passwordMatches = passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid password.");
        }

        // UUID 생성 및 저장
        String uuid = UUID.randomUUID().toString();
        user.setUserId(uuid);
        usersRepository.save(user);

        // JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createTokens(loginRequestDto.getEmail(), user.getRole().getRoleName());

        // 토큰과 UUID 반환
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", tokenDto.getAccessToken());
        responseBody.put("refreshToken", tokenDto.getRefreshToken());
        responseBody.put("userId", uuid);

        return responseBody;
    }
    public Map<String, String> socialLogin(String email) {
        // 사용자 검색
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // UUID 생성 및 저장
        String uuid = UUID.randomUUID().toString();
        user.setUserId(uuid);
        usersRepository.save(user);

        // JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createTokens(email, user.getRole().getRoleName());

        // 토큰과 UUID 반환
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", tokenDto.getAccessToken());
        responseBody.put("refreshToken", tokenDto.getRefreshToken());
        responseBody.put("userId", uuid);

        return responseBody;
    }

    @Override
    public void updatePassword(String userId, String password) {
        Optional<Users> existingUser = usersRepository.findByUserId(userId);
        if(existingUser.isPresent()) {
            existingUser.get().setPassword(passwordEncoder.encode(password));
            usersRepository.save(existingUser.get());
        }
    }

    @Override
    public UserDto.UserInfoResponseDto getUserInfo(String userId) {
        Optional<Users> existingUser = usersRepository.findByUserId(userId);

        UserDto.UserInfoResponseDto userInfoResponseDto = new UserDto.UserInfoResponseDto();
        if(existingUser.isPresent()) {
            userInfoResponseDto.setNickname(existingUser.get().getNickname());
            userInfoResponseDto.setEmail(existingUser.get().getEmail());
            userInfoResponseDto.setBirthdate(existingUser.get().getBirthdate());
            userInfoResponseDto.setRole(existingUser.get().getRole());
            userInfoResponseDto.setProvider(existingUser.get().getProvider());
        }

        return userInfoResponseDto;
    }
}

