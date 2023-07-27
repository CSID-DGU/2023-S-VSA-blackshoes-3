package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.entity.Role;
import com.travelvcommerce.userservice.entity.Users;
import com.travelvcommerce.userservice.repository.UsersRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final UsersRepository usersRepository;

    private final CustomUserDetailsService userDetailsService;

    @Override
    public Map<String, String> registerUser(UserDto.UserRegisterRequestDto registerRequestDto) {
        if(usersRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        Users user = new Users();
        user.setUserId(UUID.randomUUID().toString());
        user.setEmail(registerRequestDto.getEmail());
        user.setNickname(registerRequestDto.getNickname());
        user.setBirthdate(registerRequestDto.getBirthdate());
        user.setRole(Role.valueOf("USER"));
        user.setProvider(registerRequestDto.getProvider());
        user.setProviderId(registerRequestDto.getProviderId());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));  // 비밀번호 암호화
        usersRepository.save(user);

        UserDto.UserRegisterResponseDto userRegisterResponseDto = new UserDto.UserRegisterResponseDto();

        userRegisterResponseDto.setUserId(user.getUserId());
        userRegisterResponseDto.setCreatedAt(user.getCreatedAt());
        Map<String, String> responseBody = new HashMap<>();

        responseBody.put("userId", userRegisterResponseDto.getUserId());
        responseBody.put("createdAt", userRegisterResponseDto.getFormattedCreatedAt());

        return responseBody;
    }
    @Override
    public Map<String, String> updateUser(String userId, UserDto userDto){
        Optional<Users> existingUser = usersRepository.findByUserId(userId);
        if(existingUser.isPresent()) {
            existingUser.get().setNickname(userDto.getNickname());
            existingUser.get().setPassword(passwordEncoder.encode(userDto.getPassword()));
            existingUser.get().setBirthdate(userDto.getBirthdate());
            usersRepository.save(existingUser.get());
        }

        UserDto.UserUpdateResponseDto responseDto = new UserDto.UserUpdateResponseDto();

        Map<String, String> responseBody = new HashMap<>();


        responseBody.put("userId", responseDto.getUserId());
        responseBody.put("updatedAt", responseDto.getFormattedUpdatedAt());

        return responseBody;
    }


    @Override
    public void deleteUser(String userId) {
        usersRepository.deleteByUserId(userId);
    }
    @Override
    public Map<String, String> login(UserDto.UserLoginRequestDto loginRequestDto) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!loginRequestDto.getEmail().matches(emailRegex)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바른 이메일 형식이 아닙니다.");
        }

        // 사용자 검색
        Users user = usersRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

        // 비밀번호 검증
        boolean passwordMatches = passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid password.");
        }

        // JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createTokens(loginRequestDto.getEmail(), user.getRole().getRoleName());

        // 토큰과 UUID 반환
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", tokenDto.getAccessToken());
        responseBody.put("refreshToken", tokenDto.getRefreshToken());

        return responseBody;
    }
    public Map<String, String> socialLogin(String email) {
        // 사용자 검색
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if(user.getPassword().equals("")) {
            throw new BadCredentialsException("Invalid password.");
        }

        // JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createTokens(email, user.getRole().getRoleName());

        // 토큰만 반환
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", tokenDto.getAccessToken());
        responseBody.put("refreshToken", tokenDto.getRefreshToken());

        return responseBody;
    }

    @Override
    public Map<String, String> updatePassword(String userId, String password) {
        Optional<Users> existingUser = usersRepository.findByUserId(userId);
        if(existingUser.isPresent()) {
            existingUser.get().setPassword(passwordEncoder.encode(password));
            usersRepository.save(existingUser.get());
        }

        UserDto.UserUpdateResponseDto responseDto = new UserDto.UserUpdateResponseDto();

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("updatedAt", responseDto.getFormattedUpdatedAt());
        responseBody.put("userId", responseDto.getUserId());

        return responseBody;
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

