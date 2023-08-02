package com.travelvcommerce.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.userservice.dto.ResponseDto;
import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.entity.User;
import com.travelvcommerce.userservice.repository.RefreshTokenRepository;
import com.travelvcommerce.userservice.repository.UserRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import com.travelvcommerce.userservice.service.EmailService;
import com.travelvcommerce.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/user-service/users")
@RequiredArgsConstructor
public class UserController {
    private final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,16}$";

    private final String nicknameRegex = "^[가-힣A-Za-z0-9]{1,10}$";
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/join")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody UserDto.UserRegisterRequestDto registerRequestDto) {
        try {
            if(!emailService.isExistCompletionCode(registerRequestDto.getEmail())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("인증코드를 재확인하세요.").build());
            }

            if(userRepository.existsByEmail(registerRequestDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("이미 가입된 이메일입니다.").build());
            }

            String password = registerRequestDto.getPassword();
            if(!password.matches(passwordRegex)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("비밀번호는 8~16자리의 영문, 숫자, 특수문자 조합이어야 합니다.").build());
            }

            String nickname = registerRequestDto.getNickname();
            if(!nickname.matches(nicknameRegex)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("닉네임은 특수문자를 포함하지 않는 1~10자리의 한글, 영문, 숫자 조합이어야 합니다.").build());
            }

            Map<String, String> userRegisterResponse = userService.registerUser(registerRequestDto);
            emailService.deleteCompletionCode(registerRequestDto.getEmail());

            ResponseDto responseDto = ResponseDto.builder().payload(userRegisterResponse).build();

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    //이메일, 닉네임, 생일
    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDto> updateUser(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable String userId,
                                                  @RequestBody UserDto.UserUpdateRequestDto userUpdateRequestDto) {
        try {
            String bearerToken = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
            if (!jwtTokenProvider.validateToken(bearerToken, userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            String tokenUserEmail = jwtTokenProvider.getEmailFromToken(bearerToken);

            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }
            String userEmail = userOptional.get().getEmail();

            if (!userEmail.equals(tokenUserEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }


            String nickname = userUpdateRequestDto.getNickname();
            if(!nickname.matches(nicknameRegex)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("닉네임은 특수문자를 포함하지 않는 1~10자리의 한글, 영문, 숫자 조합이어야 합니다.").build());
            }

            Map<String, String> userUpdateResponse = userService.updateUser(userId, userUpdateRequestDto);

            ResponseDto responseDto = ResponseDto.builder()
                    .payload(userUpdateResponse)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);

        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto> deleteUser(@RequestHeader("Authorization") String accessToken, @PathVariable String userId, @RequestBody UserDto.UserDeleteRequestDto userDeleteRequestDto){
        try {
            String bearerToken = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;

            if (!jwtTokenProvider.validateToken(bearerToken, userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            String tokenUserEmail = jwtTokenProvider.getEmailFromToken(bearerToken);
            String tokenUserType = jwtTokenProvider.getUserTypeFromToken(bearerToken);

            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }
            String userEmail = userOptional.get().getEmail();

            if (!userEmail.equals(tokenUserEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }

            if(!passwordEncoder.matches(userDeleteRequestDto.getPassword(), userOptional.get().getPassword())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid Password").build());
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
            if (!userLoginRequestDto.getEmail().matches(emailRegex)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("올바른 이메일 형식이 아닙니다.").build());
            }

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

    @PutMapping("/{userId}/password/update")
    public ResponseEntity<ResponseDto> updatePassword(@RequestHeader("Authorization") String token,
                                                      @PathVariable String userId,
                                                      @RequestBody UserDto.UserUpdatePasswordRequestDto userUpdatePasswordRequestDto){
        try {
            String bearerToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            if (!jwtTokenProvider.validateToken(bearerToken, userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            String email = userUpdatePasswordRequestDto.getEmail();
            if(!emailService.isExistCompletionCode(email)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("인증코드를 재확인하세요.").build());
            }

            String password = userUpdatePasswordRequestDto.getPassword();
            if(!password.matches(passwordRegex)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("비밀번호는 8자 이상 16자 이하의 영문, 숫자, 특수문자를 포함해야 합니다.").build());
            }

            Map<String, String> updateResponse = userService.updatePassword(userId, userUpdatePasswordRequestDto.getPassword());
            emailService.deleteCompletionCode(email);

            ResponseDto responseDto = ResponseDto.builder().payload(updateResponse).build();
            return ResponseEntity.ok().body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @PutMapping("/password/find")
    public ResponseEntity<ResponseDto> findPassword(@RequestBody UserDto.UserFindPasswordRequestDto userFindPasswordRequestDto){
        try {

            String email = userFindPasswordRequestDto.getEmail();

            if(!emailService.isExistCompletionCode(email)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("인증코드를 재확인하세요.").build());
            }

            String password = userFindPasswordRequestDto.getPassword();
            if(!password.matches(passwordRegex)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("비밀번호는 8자 이상 16자 이하의 영문, 숫자, 특수문자를 포함해야 합니다.").build());
            }

            Map<String, String> updateResponse = userService.findPassword(email, userFindPasswordRequestDto.getPassword());
            emailService.deleteCompletionCode(email);

            ResponseDto responseDto = ResponseDto.builder().payload(updateResponse).build();
            return ResponseEntity.ok().body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto> getUserInfo(@RequestHeader("Authorization") String accessToken, @PathVariable String userId) {
        try {
            String bearerToken = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;

            if (!jwtTokenProvider.validateToken(bearerToken, userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            String tokenUserEmail = jwtTokenProvider.getEmailFromToken(bearerToken);

            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }
            String userEmail = userOptional.get().getEmail();

            if (!userEmail.equals(tokenUserEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }


            UserDto.UserInfoResponseDto userInfoResponseDto = userService.getUserInfo(userId);

            ResponseDto responseDto = ResponseDto.builder()
                    .payload(objectMapper.convertValue(userInfoResponseDto, Map.class))
                    .build();

            return ResponseEntity.ok().body(responseDto);

        } catch (UsernameNotFoundException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }
}