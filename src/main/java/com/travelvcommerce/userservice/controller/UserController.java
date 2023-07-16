package com.travelvcommerce.userservice.controller;

import com.travelvcommerce.userservice.dto.EmailDto;
import com.travelvcommerce.userservice.dto.ResponseDto;
import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.entity.Users;
import com.travelvcommerce.userservice.repository.RefreshTokenRepository;
import com.travelvcommerce.userservice.repository.UsersRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import com.travelvcommerce.userservice.service.CustomUserDetailsService;
import com.travelvcommerce.userservice.service.EmailService;
import com.travelvcommerce.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/user-service/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository usersRepository;
    private final RefreshTokenRepository refreshTokenRepository;

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
    public ResponseEntity<ResponseDto> updateUser(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable String userId,
                                                  @RequestBody UserDto userDto) {
        try {
            String bearerToken = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
            if (!jwtTokenProvider.validateToken(bearerToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            String tokenUserEmail = jwtTokenProvider.getEmailFromToken(bearerToken);

            Optional<Users> userOptional = usersRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }
            String userEmail = userOptional.get().getEmail();

            if (!userEmail.equals(tokenUserEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }

            userService.updateUser(userId, userDto);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto> deleteUser(@RequestHeader("Authorization") String accessToken, @PathVariable String userId) {
        try {
            String bearerToken = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;

            if (!jwtTokenProvider.validateToken(bearerToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            String tokenUserEmail = jwtTokenProvider.getEmailFromToken(bearerToken);
            String tokenUserType = jwtTokenProvider.getUserTypeFromToken(bearerToken);

            Optional<Users> userOptional = usersRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }
            String userEmail = userOptional.get().getEmail();

            if (!userEmail.equals(tokenUserEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }

            userService.deleteUser(userId);
            refreshTokenRepository.deleteRefreshTokenByUserEmail(tokenUserType, userEmail);

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

}
