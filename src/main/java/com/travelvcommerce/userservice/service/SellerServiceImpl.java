package com.travelvcommerce.userservice.service;

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

import javax.transaction.Transactional;
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
    public void registerSeller(SellerDto.SellerRegisterRequestDto registerRequestDto) {
        if(sellerRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        Seller seller = new Seller();
        seller.setEmail(registerRequestDto.getEmail());
        seller.setCompanyName(registerRequestDto.getCompanyName());
        seller.setIcon(registerRequestDto.getIcon());
        seller.setRole(Role.valueOf("SELLER"));
        seller.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));  // 비밀번호 암호화

        sellerRepository.save(seller);
    }

    @Override
    public void updateSeller(String sellerId, SellerDto sellerDto) {
        Optional<Seller> existingSeller = sellerRepository.findBySellerId(sellerId);
        if(existingSeller.isPresent()) {
            existingSeller.get().setCompanyName(sellerDto.getCompanyName());
            existingSeller.get().setIcon(sellerDto.getIcon());
            existingSeller.get().setPassword(passwordEncoder.encode(sellerDto.getPassword()));
            sellerRepository.save(existingSeller.get());
        } else {
            throw new RuntimeException("존재하지 않는 판매자입니다.");
        }
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

        String uuid = UUID.randomUUID().toString();
        seller.setSellerId(uuid);
        sellerRepository.save(seller);

        TokenDto tokenDto = jwtTokenProvider.createTokens(loginRequestDto.getEmail(), seller.getRole().getRoleName());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", tokenDto.getAccessToken());
        responseBody.put("refreshToken", tokenDto.getRefreshToken());
        responseBody.put("sellerId", uuid);

        return responseBody;
    }

    @Override
    public void updatePassword(String sellerId, String password) {
        Optional<Seller> existingSeller = sellerRepository.findBySellerId(sellerId);
        if(existingSeller.isPresent()) {
            existingSeller.get().setPassword(passwordEncoder.encode(password));
            sellerRepository.save(existingSeller.get());
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
                .companyName(seller.getCompanyName())
                .icon(seller.getIcon())
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
                .companyName(seller.getCompanyName())
                .icon(seller.getIcon())
                .build();
        return sellerUploaderInfoDto;
    }
}
