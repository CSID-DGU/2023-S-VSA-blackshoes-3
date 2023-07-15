package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.entity.Role;
import com.travelvcommerce.userservice.entity.Users;
import com.travelvcommerce.userservice.repository.UsersRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UsersRepository usersRepository;


    @Override
    public void registerUser(UserDto.UserRegisterRequestDto registerRequestDto) {
        if(usersRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        Users user = new Users();
        user.setEmail(registerRequestDto.getEmail());
        user.setName(registerRequestDto.getName());
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
            existingUser.get().setName(userDto.getName());
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

    @Override
    public void findPassword(String userId) {
        //이메일로 인증번호 받기

    }
    private Users convertToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        Users user = new Users();
        user.setId(userDto.getId());
        user.setUserId(userDto.getUserId());
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setBirthdate(userDto.getBirthdate());
        user.setRole(userDto.getRole());
        user.setProvider(userDto.getProvider());
        user.setProviderId(userDto.getProviderId());
        return user;
    }

    private UserDto convertToUserDto(Users user, TokenDto tokenDto, Collection<? extends GrantedAuthority> authorities) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserId(user.getUserId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setBirthdate(user.getBirthdate());
        userDto.setRole(user.getRole());
        userDto.setProvider(user.getProvider());
        userDto.setProviderId(user.getProviderId());
        userDto.setTokenDto(tokenDto);  // TokenDto 설정
        userDto.setAuthorities(authorities);
        return userDto;
    }
    public String getUserEmailByUserId(String userId) {
        return usersRepository.findByUserId(userId)
                .map(Users::getEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
    }
}

