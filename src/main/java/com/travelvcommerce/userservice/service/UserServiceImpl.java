package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.dto.request.LoginRequestDto;
import com.travelvcommerce.userservice.dto.request.RegisterRequestDto;
import com.travelvcommerce.userservice.entity.Users;
import com.travelvcommerce.userservice.repository.UsersRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void registerUser(RegisterRequestDto registerRequestDto) {
        Users user = new Users();
        user.setEmail(registerRequestDto.getEmail());
        user.setName(registerRequestDto.getName());
        user.setBirthdate(registerRequestDto.getBirthdate());
        user.setRole(registerRequestDto.getRole());
        user.setProvider(registerRequestDto.getProvider());
        user.setProviderId(registerRequestDto.getProviderId());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));  // 비밀번호 암호화
        usersRepository.save(user);
    }


    @Override
    public void updateUser(String userId, UserDto userDto) {
        Optional<Users> existingUser = usersRepository.findById(userId);
        if (existingUser.isPresent()) {
            Users updatedUser = convertToUser(userDto);
            usersRepository.save(updatedUser);
        }
    }

    @Override
    public void deleteUser(String userId) {
        usersRepository.deleteById(userId);
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        // 사용자 검색
        Users user = usersRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

        // 비밀번호 검증
        boolean passwordMatches = passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid password.");
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(loginRequestDto.getEmail());

        // 토큰 반환
        return token;
    }

    @Override
    public void findPassword(String userId) {
        // find password logic
    }
    private UserDto convertToUserDto(Users user, String token, Collection<? extends GrantedAuthority> authorities) {
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
        userDto.setToken(token);
        userDto.setAuthorities(authorities);
        return userDto;
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
}
