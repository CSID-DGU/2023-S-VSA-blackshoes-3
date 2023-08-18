package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.ResponseDto;
import com.travelvcommerce.userservice.dto.SellerDto;
import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.entity.Role;
import com.travelvcommerce.userservice.entity.Seller;
import com.travelvcommerce.userservice.repository.SellerRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class SellerServiceImpl implements SellerService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SellerRepository sellerRepository;

    @Override
    public SellerDto registerSeller(SellerDto.SellerRegisterRequestDto registerRequestDto) {
        if (sellerRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        Seller seller = Seller.builder()
                .sellerId(UUID.randomUUID().toString())
                .email(registerRequestDto.getEmail())
                .sellerName(registerRequestDto.getSellerName())
                .sellerLogo(registerRequestDto.getSellerLogo())
                .role(Role.valueOf("SELLER"))
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sellerRepository.save(seller);

        SellerDto sellerDto = SellerDto.builder()
                .sellerId(seller.getSellerId())
                .email(seller.getEmail())
                .sellerName(seller.getSellerName())
                .sellerLogo(seller.getSellerLogo())
                .createdAt(seller.getCreatedAt())
                .updatedAt(seller.getUpdatedAt())
                .build();

        return sellerDto;
    }

    @Override
    public SellerDto updateSellerName(String sellerId, SellerDto.SellerUpdateRequestDto sellerUpdateRequestDto) {
        Seller existingSeller = sellerRepository.findBySellerId(sellerId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 판매자입니다."));

        try {
            existingSeller.updateSellerName(sellerUpdateRequestDto.getSellerName());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("판매자명을 처리하는데 실패했습니다", e);
        }

        SellerDto sellerDto = SellerDto.builder()
                .sellerId(existingSeller.getSellerId())
                .email(existingSeller.getEmail())
                .sellerName(existingSeller.getSellerName())
                .sellerLogo(existingSeller.getSellerLogo())
                .createdAt(existingSeller.getCreatedAt())
                .updatedAt(existingSeller.getUpdatedAt())
                .build();

        return sellerDto;
    }

    @Override
    public SellerDto updateSellerLogo(String sellerId, MultipartFile sellerLogo) {
        Seller existingSeller = sellerRepository.findBySellerId(sellerId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 판매자입니다."));

        try {
            existingSeller.updateSellerLogo(sellerLogo.getBytes());
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new RuntimeException("판매자 로고를 처리하는 데 실패했습니다.", e);
        }

        SellerDto sellerDto = SellerDto.builder()
                .sellerId(existingSeller.getSellerId())
                .email(existingSeller.getEmail())
                .sellerName(existingSeller.getSellerName())
                .sellerLogo(existingSeller.getSellerLogo())
                .createdAt(existingSeller.getCreatedAt())
                .updatedAt(existingSeller.getUpdatedAt())
                .build();

        return sellerDto;
    }

    @Override
    public void deleteSeller(String sellerId) {
        sellerRepository.deleteBySellerId(sellerId);
    }

    @Override
    public Map<String, String> login(SellerDto.SellerLoginRequestDto loginRequestDto) {
        Seller seller = sellerRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Seller not found with email: " + loginRequestDto.getEmail()));

        boolean passwordMatches = passwordEncoder.matches(loginRequestDto.getPassword(), seller.getPassword());
        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid password.");
        }

        TokenDto tokenDto = jwtTokenProvider.createTokens(loginRequestDto.getEmail(), seller.getRole().getRoleName(), seller.getSellerId());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("sellerId", seller.getSellerId());
        responseBody.put("accessToken", tokenDto.getAccessToken());
        responseBody.put("refreshToken", tokenDto.getRefreshToken());

        return responseBody;
    }

    @Override
    public Map<String, String> updatePassword(String sellerId, SellerDto.SellerUpdatePasswordRequestDto updatePasswordRequestDto) {
        Seller existingSeller = sellerRepository.findBySellerId(sellerId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 판매자입니다."));

        String oldPassword = updatePasswordRequestDto.getOldPassword();
        String newPassword = updatePasswordRequestDto.getNewPassword();
        String password = existingSeller.getPassword();

        if (!passwordEncoder.matches(oldPassword, password)) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }
        existingSeller.setPassword(passwordEncoder.encode(newPassword));
        existingSeller.setUpdatedAt(LocalDateTime.now());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("sellerId", existingSeller.getSellerId());
        responseBody.put("updatedAt", existingSeller.getUpdatedAt().toString());
        return responseBody;
    }

    @Override
    public SellerDto.SellerInfoDto getSellerInfo(String sellerId) {
        Seller seller;
        try {
            seller = sellerRepository.findBySellerId(sellerId).orElseThrow(() -> new RuntimeException("존재하지 않는 판매자입니다."));
        } catch (RuntimeException e) {
            throw new RuntimeException("존재하지 않는 판매자입니다.");
        }
        SellerDto.SellerInfoDto sellerInfoDto = SellerDto.SellerInfoDto.builder()
                .sellerId(seller.getSellerId())
                .email(seller.getEmail())
                .sellerName(seller.getSellerName())
                .sellerLogo(seller.getSellerLogo())
                .build();
        return sellerInfoDto;
    }

    public Map<String, String> findPassword(String email, String password) {
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Seller not found with email: " + email));

        seller.setPassword(passwordEncoder.encode(password));
        seller.setUpdatedAt(LocalDateTime.now());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("sellerId", seller.getSellerId());
        responseBody.put("updatedAt", seller.getUpdatedAt().toString());
        return responseBody;
    }
}
