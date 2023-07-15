package com.travelvcommerce.userservice.controller;


import com.travelvcommerce.userservice.dto.ResponseDto;
import com.travelvcommerce.userservice.dto.SellerDto;
import com.travelvcommerce.userservice.entity.Seller;
import com.travelvcommerce.userservice.entity.Users;
import com.travelvcommerce.userservice.repository.RefreshTokenRepository;
import com.travelvcommerce.userservice.repository.SellerRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import com.travelvcommerce.userservice.service.SellerDetailsService;
import com.travelvcommerce.userservice.service.SellerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user-service/seller")
public class SellerController {

    @Autowired
    private SellerServiceImpl sellerService;

    @Autowired
    private SellerDetailsService sellerDetailsService;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto> registerSeller(@RequestParam("email") String email,
                                                      @RequestParam("password") String password,
                                                      @RequestParam("companyName") String companyName,
                                                      @RequestParam("icon") MultipartFile icon) {
        try {
            SellerDto.SellerRegisterRequestDto sellerRegisterRequestDto = new SellerDto.SellerRegisterRequestDto();
            sellerRegisterRequestDto.setEmail(email);
            sellerRegisterRequestDto.setPassword(password);
            sellerRegisterRequestDto.setCompanyName(companyName);
            sellerRegisterRequestDto.setIcon(icon.getBytes());

            sellerService.registerSeller(sellerRegisterRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
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

            String tokenSellerEmail = jwtTokenProvider.getUsernameFromToken(bearerToken);
            String tokenSellerType = jwtTokenProvider.getUserTypeFromToken(bearerToken);

            Optional<Seller> sellerOptional = sellerRepository.findBySellerId(sellerId);
            if (!sellerOptional.isPresent()) {
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
                                                    @RequestParam("password") String password,
                                                    @RequestParam("companyName") String companyName,
                                                    @RequestParam("icon") MultipartFile icon) {
        try {
            String bearerToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            if (!jwtTokenProvider.validateToken(bearerToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().error("Invalid token").build());
            }

            // You'll need a similar check for the seller here, similar to updateUser

            SellerDto sellerDto = new SellerDto();
            sellerDto.setPassword(password);
            sellerDto.setCompanyName(companyName);
            sellerDto.setIcon(icon.getBytes());

            sellerService.updateSeller(sellerId, sellerDto);
            return ResponseEntity.ok().build();
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

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody SellerDto.SellerLoginRequestDto sellerLoginRequestDto) {
        try {
            UserDetails sellerDetails = sellerDetailsService.loadUserByUsername(sellerLoginRequestDto.getEmail());

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
}
