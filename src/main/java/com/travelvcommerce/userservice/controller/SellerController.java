package com.travelvcommerce.userservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.userservice.dto.ResponseDto;
import com.travelvcommerce.userservice.dto.SellerDto;
import com.travelvcommerce.userservice.entity.Seller;
import com.travelvcommerce.userservice.repository.RefreshTokenRepository;
import com.travelvcommerce.userservice.repository.SellerRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import com.travelvcommerce.userservice.service.EmailService;
import com.travelvcommerce.userservice.service.kafka.KafkaUploaderInfoProducerService;
import com.travelvcommerce.userservice.service.SellerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user-service/sellers")
@RequiredArgsConstructor
public class SellerController {
    private final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,16}$";
    private final String sellerNameRegex = "^[가-힣A-Za-z0-9]{1,10}$";

    private final SellerServiceImpl sellerService;
    private final SellerRepository sellerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final KafkaUploaderInfoProducerService kafkaUploaderInfoProducerService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto> registerSeller(@RequestPart(name = "joinRequest") SellerDto.SellerRegisterRequestDto sellerRegisterRequestDto,
                                                      @RequestPart(name = "sellerLogo") MultipartFile sellerLogo) {
        try {
            if(!emailService.isExistCompletionCode(sellerRegisterRequestDto.getEmail())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("인증코드를 재확인하세요.").build());
            }

            if(sellerRepository.existsByEmail(sellerRegisterRequestDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("이미 가입된 이메일입니다.").build());
            }

            String password = sellerRegisterRequestDto.getPassword();
            if(!password.matches(passwordRegex)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("비밀번호는 8~16자리의 영문, 숫자, 특수문자 조합이어야 합니다.").build());
            }

            String sellerName = sellerRegisterRequestDto.getSellerName();
            if(!sellerName.matches(sellerNameRegex)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("판매자명은 특수문자를 포함하지 않는 1~10자리의 한글, 영문, 숫자 조합이어야 합니다.").build());
            }

            // MultipartFile를 byte 배열로 변환
            byte[] sellerLogoBytes = sellerLogo.getBytes();
            if(sellerLogoBytes.length > 16777215){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("판매자 로고는 16MB 이하의 파일만 업로드 가능합니다.").build());
            }
            sellerRegisterRequestDto.setSellerLogo(sellerLogoBytes);

            // SellerService를 이용하여 저장된
            Map<String, String> sellerRegisterResponse = sellerService.registerSeller(sellerRegisterRequestDto);
            emailService.deleteCompletionCode(sellerRegisterRequestDto.getEmail());

            // 생성된 uploader 정보를 kafka topic에 publish
            String sellerId = sellerRegisterResponse.get("sellerId");

            Seller seller = sellerRepository.findBySellerId(sellerId).get();

            SellerDto.SellerInfoDto uploaderInfoDto = SellerDto.SellerInfoDto.builder()
                    .sellerId(sellerId)
                    .sellerName(seller.getSellerName())
                    .sellerLogo(seller.getSellerLogo())
                    .build();

            kafkaUploaderInfoProducerService.createUploader(uploaderInfoDto);

            ResponseDto responseDto = ResponseDto.builder().payload(sellerRegisterResponse).build();

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (RuntimeException | IOException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @DeleteMapping("/{sellerId}")
    public ResponseEntity<ResponseDto> deleteSeller(@RequestHeader("Authorization") String accessToken,
                                                    @PathVariable String sellerId,
                                                    @RequestBody SellerDto.SellerDeleteRequestDto sellerDeleteRequestDto) {
        try {
            String bearerToken = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
            if (!jwtTokenProvider.validateToken(bearerToken, sellerId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            String tokenSellerEmail = jwtTokenProvider.getEmailFromToken(bearerToken);
            String tokenSellerType = jwtTokenProvider.getUserTypeFromToken(bearerToken);

            Optional<Seller> sellerOptional = sellerRepository.findBySellerId(sellerId);
            if (sellerOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }
            String sellerEmail = sellerOptional.get().getEmail();

            if (!sellerEmail.equals(tokenSellerEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid UserId").build());
            }

            if(!passwordEncoder.matches(sellerDeleteRequestDto.getPassword(), sellerOptional.get().getPassword())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().error("Invalid Password").build());
            }

            sellerService.deleteSeller(sellerId);
            refreshTokenRepository.deleteRefreshTokenByUserEmail(tokenSellerType, sellerEmail);

            // 삭제된 판매자의 정보를 kafka topic에 publish
            kafkaUploaderInfoProducerService.deleteUploader(sellerId);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @PutMapping("/{sellerId}")
    public ResponseEntity<ResponseDto> updateSeller(@RequestHeader("Authorization") String token,
                                                    @PathVariable String sellerId,
                                                    @RequestPart(name = "updateRequest") SellerDto.SellerUpdateRequestDto sellerUpdateRequestDto,
                                                    @RequestPart(name = "sellerLogo") MultipartFile sellerLogo) {
        try {
            String bearerToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            if (!jwtTokenProvider.validateToken(bearerToken, sellerId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            String sellerName = sellerUpdateRequestDto.getSellerName();
            if(!sellerName.matches(sellerNameRegex)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("판매자명은 특수문자를 포함하지 않는 1~10자리의 한글, 영문, 숫자 조합이어야 합니다.").build());
            }

            // MultipartFile를 byte 배열로 변환
            byte[] sellerLogoBytes = sellerLogo.getBytes();
            if(sellerLogoBytes.length > 16777215){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("판매자 로고는 16MB 이하의 파일만 업로드 가능합니다.").build());
            }

            // SellerService를 이용하여 저장
            Map<String, String> sellerUpdateResponse = sellerService.updateSeller(sellerId ,sellerUpdateRequestDto, sellerLogo);

            // 변경된 정보를 kafka topic에 publish
            Seller seller = sellerRepository.findBySellerId(sellerId).get();

            SellerDto.SellerInfoDto uploaderInfoDto = SellerDto.SellerInfoDto.builder()
                    .sellerId(sellerId)
                    .sellerName(seller.getSellerName())
                    .sellerLogo(seller.getSellerLogo())
                    .build();

            kafkaUploaderInfoProducerService.updateUploader(uploaderInfoDto);


            ResponseDto responseDto = ResponseDto.builder().payload(sellerUpdateResponse).build();

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

        } catch (EntityNotFoundException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (Exception e) {
            System.out.println(e);
            ResponseDto responseDto = ResponseDto.builder().error("서버 내부 오류").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody SellerDto.SellerLoginRequestDto sellerLoginRequestDto) {
        try {
            Map<String, String> loginResponse = sellerService.login(sellerLoginRequestDto);

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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
        }
    }

    @PutMapping("/{sellerId}/password")
    public ResponseEntity<ResponseDto> updatePassword(@PathVariable String sellerId,
                                                      @RequestHeader("Authorization") String token,
                                                      @RequestBody SellerDto.SellerUpdatePasswordRequestDto sellerUpdatePasswordRequestDto){
        try {
            String bearerToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            if (!jwtTokenProvider.validateToken(bearerToken, sellerId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            if(!emailService.isExistVerificationCode(sellerUpdatePasswordRequestDto.getEmail())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("인증코드를 확인해주세요.").build());
            }

            String password = sellerUpdatePasswordRequestDto.getPassword();
            if(!password.matches(passwordRegex)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error("비밀번호는 8~16자리의 영문, 숫자, 특수문자 조합이어야 합니다.").build());
            }

            Map<String, String> updatePasswordResponse = sellerService.updatePassword(sellerId, sellerUpdatePasswordRequestDto.getPassword());

            ResponseDto responseDto = ResponseDto.builder()
                    .payload(updatePasswordResponse)
                    .build();

            return ResponseEntity.ok().body(responseDto);

        } catch (EntityNotFoundException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder().error("서버 내부 오류").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<ResponseDto> getSellerInfo(@PathVariable String sellerId) {
        SellerDto.SellerInfoDto sellerInfoDto;

        try {
            sellerInfoDto = sellerService.getSellerInfo(sellerId);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.builder()
                .payload(objectMapper.convertValue(sellerInfoDto, Map.class)).build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
