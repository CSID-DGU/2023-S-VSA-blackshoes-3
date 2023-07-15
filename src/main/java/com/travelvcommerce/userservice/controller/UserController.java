package com.travelvcommerce.userservice.controller;

import com.travelvcommerce.userservice.dto.ResponseDto;
import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.entity.Users;
import com.travelvcommerce.userservice.repository.RefreshTokenRepository;
import com.travelvcommerce.userservice.repository.UsersRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import com.travelvcommerce.userservice.service.CustomUserDetailsService;
import com.travelvcommerce.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/user-service/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody UserDto.UserRegisterRequestDto registerRequestDto) {
        try {
            userService.registerUser(registerRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable String userId, @RequestBody UserDto userDto) {
        try {
            userService.updateUser(userId, userDto);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto> deleteUser(@RequestHeader("Authorization") String token, @PathVariable String userId) {
        try {
            String bearerToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            if (!jwtTokenProvider.validateToken(bearerToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            String tokenUserEmail = jwtTokenProvider.getUsernameFromToken(bearerToken);

            Optional<Users> userOptional = usersRepository.findByUserId(userId);
            if (!userOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }
            String userEmail = userOptional.get().getEmail();

            if (!userEmail.equals(tokenUserEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }

            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UsernameNotFoundException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }



    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody UserDto.UserLoginRequestDto userLoginRequestDto) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginRequestDto.getEmail());
            Map<String, String> loginResponse = userService.login(userLoginRequestDto);

            ResponseDto responseDto = ResponseDto.builder()
                    .payload(loginResponse)
                    .build();

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + loginResponse.get("token"))
                    .body(responseDto);

        } catch (UsernameNotFoundException e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        } catch (BadCredentialsException e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }
    }

    @PostMapping("/{userId}/password")
    public ResponseEntity<ResponseDto> findPassword(@PathVariable String userId) {
        try {
            userService.findPassword(userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @PostMapping("/{userId}/refresh")
    public ResponseEntity<ResponseDto> refreshToken(@PathVariable String userId, @RequestBody Map<String, Object> payload) {
        try {
            Map<String, Object> payloadMap = (Map<String, Object>) payload.get("payload");
            String refreshToken = (String) payloadMap.get("refreshToken");

            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseDto.builder()
                        .error("리프레시 토큰은 비어 있거나 null일 수 없습니다.")
                        .build());
            }

            // 1. 가져온 userId로 email을 찾는다.
            String email = userService.getUserEmailByUserId(userId);

            // 2. email로 refreshToken이 있는지 찾는다.
            String existingRefreshToken = refreshTokenRepository.find(email);
            if (existingRefreshToken == null || existingRefreshToken.isEmpty()) {
                throw new BadCredentialsException("리프레시 토큰이 존재하지 않습니다.");
            }

            // 3. refreshToken 내의 email과 요청 유저의 email이 일치하는지 검사한다.
            String emailFromToken = jwtTokenProvider.getUsernameFromToken(refreshToken);
            if (!emailFromToken.equals(email)) {
                throw new BadCredentialsException("리프레시 토큰이 일치하지 않습니다.");
            }

            // 4. refreshToken이 존재한다면 newAccessToken을 발급한다.
            String newAccessToken = jwtTokenProvider.createAccessToken(email);
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + newAccessToken)
                    .body(ResponseDto.builder()
                            .payload(new HashMap<String, Object>() {{
                                put("refreshToken", refreshToken);
                                put("accessToken", newAccessToken);
                            }})
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder()
                    .error("토큰 갱신 오류: " + e.getMessage())
                    .build());
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestBody Map<String, String> payload) {
        try {
            String refreshToken = payload.get("refreshToken");
            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseDto.builder()
                        .error("리프레시 토큰은 비어 있거나 null일 수 없습니다.")
                        .build());
            }

            boolean isValid = jwtTokenProvider.validateToken(refreshToken);
            if (!isValid) {
                throw new BadCredentialsException("유효하지 않은 리프레시 토큰입니다.");
            }

            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            //jwtTokenProvider.deleteToken(username); TODO : 토큰 삭제, redis test 포함

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder()
                    .error("로그아웃 오류: " + e.getMessage())
                    .build());
        }
    }
}
