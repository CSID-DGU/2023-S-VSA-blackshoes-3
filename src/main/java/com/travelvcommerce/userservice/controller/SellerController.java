package com.travelvcommerce.userservice.controller;


import com.travelvcommerce.userservice.dto.ResponseDto;
import com.travelvcommerce.userservice.dto.SellerDto;
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

@RestController
@RequestMapping("/user-service/seller")
public class SellerController {

    @Autowired
    private SellerServiceImpl sellerService;

    @Autowired
    private SellerDetailsService sellerDetailsService;

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

    @PostMapping("/{sellerId}")
    public ResponseEntity<ResponseDto> deleteSeller(@RequestBody String sellerId) {
        try {
            sellerService.deleteSeller(sellerId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

    @PutMapping("/{sellerId}")
    public ResponseEntity<ResponseDto> updateSeller(@PathVariable String sellerId, @RequestBody SellerDto sellerDto) {
        try {
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
