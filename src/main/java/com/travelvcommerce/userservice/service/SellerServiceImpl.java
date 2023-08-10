package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.ResponseDto;
import com.travelvcommerce.userservice.dto.SellerDto;
import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.entity.Role;
import com.travelvcommerce.userservice.entity.Seller;
import com.travelvcommerce.userservice.repository.SellerRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;



@Transactional
@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SellerRepository sellerRepository;

    @Override
    public Map<String, String> registerSeller(SellerDto.SellerRegisterRequestDto registerRequestDto) {
        if(sellerRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        Seller seller = new Seller();
        seller.setSellerId(UUID.randomUUID().toString());
        seller.setEmail(registerRequestDto.getEmail());
        seller.setSellerName(registerRequestDto.getSellerName());
        seller.setSellerLogo(registerRequestDto.getSellerLogo());
        seller.setRole(Role.valueOf("SELLER"));
        seller.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));  // 비밀번호 암호화

        sellerRepository.save(seller);

        SellerDto.SellerRegisterResponseDto sellerRegisterResponseDto = new SellerDto.SellerRegisterResponseDto();
        sellerRegisterResponseDto.setSellerId(seller.getSellerId());
        sellerRegisterResponseDto.setCreatedAt(seller.getCreatedAt());
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("sellerId", sellerRegisterResponseDto.getSellerId());
        responseBody.put("createdAt", sellerRegisterResponseDto.getFormattedCreatedAt());
        return responseBody;
    }

    @Override
    public Map<String, String> updateSeller(String sellerId, SellerDto.SellerUpdateRequestDto sellerUpdateRequestDto, MultipartFile sellerLogo){
        Optional<Seller> existingSeller = sellerRepository.findBySellerId(sellerId);
        if(existingSeller.isPresent()) {
            existingSeller.get().setSellerName(sellerUpdateRequestDto.getSellerName());
            try {
                existingSeller.get().setSellerLogo(sellerLogo.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("판매자 로고를 처리하는 데 실패했습니다.", e);
            }
            existingSeller.get().setUpdatedAt(LocalDateTime.now());
            sellerRepository.save(existingSeller.get());
        } else {
            throw new RuntimeException("존재하지 않는 판매자입니다.");
        }

        SellerDto.SellerUpdateResponseDto sellerUpdateResponseDto = new SellerDto.SellerUpdateResponseDto();
        sellerUpdateResponseDto.setSellerId(sellerId);
        sellerUpdateResponseDto.setUpdatedAt(LocalDateTime.now());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("sellerId", sellerUpdateResponseDto.getSellerId());
        responseBody.put("updatedAt", sellerUpdateResponseDto.getFormattedUpdatedAt());
        return responseBody;
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

        sellerRepository.save(seller);

        TokenDto tokenDto = jwtTokenProvider.createTokens(loginRequestDto.getEmail(), seller.getRole().getRoleName(), seller.getSellerId());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", tokenDto.getAccessToken());
        responseBody.put("refreshToken", tokenDto.getRefreshToken());

        return responseBody;
    }

    @Override
    public Map<String, String> updatePassword(String sellerId, String password) {
        Optional<Seller> existingSeller = sellerRepository.findBySellerId(sellerId);
        if(existingSeller.isPresent()) {
            existingSeller.get().setPassword(passwordEncoder.encode(password));
            sellerRepository.save(existingSeller.get());

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("sellerId", existingSeller.get().getSellerId());
            responseBody.put("updatedAt", existingSeller.get().getUpdatedAt().toString());
            return responseBody;
        } else {
            throw new RuntimeException("존재하지 않는 판매자입니다.");
        }

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

    @Override
    public SellerDto.SellerInfoDto getSellerUploaderInfo(String sellerId) {
        Seller seller;
        try {
            seller = sellerRepository.findBySellerId(sellerId).orElseThrow(() -> new RuntimeException("존재하지 않는 판매자입니다."));
        } catch (RuntimeException e) {
            throw new RuntimeException("존재하지 않는 판매자입니다.");
        }
        SellerDto.SellerInfoDto sellerUploaderInfoDto = SellerDto.SellerInfoDto.builder()
                .sellerId(seller.getSellerId())
                .sellerName(seller.getSellerName())
                .sellerLogo(seller.getSellerLogo())
                .build();
        return sellerUploaderInfoDto;
    }
}
