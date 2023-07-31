package com.travelvcommerce.userservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.userservice.dto.ResponseDto;
import com.travelvcommerce.userservice.dto.SellerDto;
import com.travelvcommerce.userservice.entity.Seller;
import com.travelvcommerce.userservice.repository.RefreshTokenRepository;
import com.travelvcommerce.userservice.repository.SellerRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import com.travelvcommerce.userservice.service.SellerDetailsService;
import com.travelvcommerce.userservice.service.SellerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user-service/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerServiceImpl sellerService;
    private final SellerDetailsService sellerDetailsService;
    private final SellerRepository sellerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto> registerSeller(@RequestPart(name = "joinRequest") SellerDto.SellerRegisterRequestDto sellerRegisterRequestDto,
                                                      @RequestPart(name = "sellerLogo") MultipartFile sellerLogo) {
        try {
            // MultipartFile를 byte 배열로 변환
            byte[] sellerLogoBytes = sellerLogo.getBytes();
            sellerRegisterRequestDto.setSellerLogo(sellerLogoBytes);

            // SellerService를 이용하여 저장
            Map<String, String> sellerRegisterResponse = sellerService.registerSeller(sellerRegisterRequestDto);

            ResponseDto responseDto = ResponseDto.builder().payload(sellerRegisterResponse).build();

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (RuntimeException | IOException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @DeleteMapping("/{sellerId}")
    public ResponseEntity<ResponseDto> deleteSeller(@RequestHeader("Authorization") String token,
                                                    @PathVariable String sellerId) {
        try {
            String bearerToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            if (!jwtTokenProvider.validateToken(bearerToken)) {
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

            sellerService.deleteSeller(sellerId);
            refreshTokenRepository.deleteRefreshTokenByUserEmail(tokenSellerType, sellerEmail);


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
            if (!jwtTokenProvider.validateToken(bearerToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            // SellerService를 이용하여 저장
            Map<String, String> sellerUpdateResponse = sellerService.updateSeller(sellerId ,sellerUpdateRequestDto, sellerLogo);

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
            //UserDetails sellerDetails = sellerDetailsService.loadUserByUsername(sellerLoginRequestDto.getEmail());
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
                                                      @RequestParam("password") String password) {
        try {
            String bearerToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            if (!jwtTokenProvider.validateToken(bearerToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            Map<String, String> updatePasswordResponse = sellerService.updatePassword(sellerId, password);


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

    @GetMapping("/{sellerId}/uploader-info")
    public ResponseEntity<ResponseDto> getSellerUploaderInfo(@PathVariable String sellerId) {
        SellerDto.SellerInfoDto sellerUploaderInfoDto;

        try {
            sellerUploaderInfoDto = sellerService.getSellerUploaderInfo(sellerId);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.builder()
                .payload(objectMapper.convertValue(sellerUploaderInfoDto, Map.class)).build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
