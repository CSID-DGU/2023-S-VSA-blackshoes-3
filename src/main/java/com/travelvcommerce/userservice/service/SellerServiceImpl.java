package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.SellerDto;
import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.entity.Seller;
import com.travelvcommerce.userservice.repository.SellerRepository;
import com.travelvcommerce.userservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
public class SellerServiceImpl implements SellerService{

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public Map<String, String> login(SellerDto.SellerLoginRequestDto sellerLoginRequestDto) {
        // 사용자 검색
        Seller seller = sellerRepository.findByEmail(sellerLoginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Seller not found with email: " + sellerLoginRequestDto.getEmail()));

        // 비밀번호 검증
        boolean passwordMatches = passwordEncoder.matches(sellerLoginRequestDto.getPassword(), seller.getPassword());
        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid password.");
        }

        // UUID 생성 및 저장
        String uuid = UUID.randomUUID().toString();
        seller.setSellerId(uuid);
        sellerRepository.save(seller);

        // JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createTokens(sellerLoginRequestDto.getEmail(), seller.getRole().getRoleName());

        // 토큰과 UUID 반환
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", tokenDto.getAccessToken());
        responseBody.put("sellerId", uuid);

        return responseBody;
    }

    @Override
    public void registerSeller(SellerDto.SellerRegisterRequestDto sellerRegisterRequestDto) {

        if(sellerRepository.existsByEmail(sellerRegisterRequestDto.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }
        Seller seller = new Seller();
        seller.setEmail(sellerRegisterRequestDto.getEmail());
        seller.setPassword(passwordEncoder.encode(sellerRegisterRequestDto.getPassword()));  // 비밀번호 암호화
        seller.setIcon(sellerRegisterRequestDto.getIcon());
        seller.setCompanyName(sellerRegisterRequestDto.getCompanyName());

        sellerRepository.save(seller);
    }

    @Override
    public void updateSeller(String sellerId, SellerDto sellerDto) {
        Optional<Seller> existingSeller = sellerRepository.findById(sellerId);
        if(existingSeller.isPresent()) {
            existingSeller.get().setEmail(sellerDto.getEmail());
            existingSeller.get().setPassword(passwordEncoder.encode(sellerDto.getPassword()));
            existingSeller.get().setIcon(sellerDto.getIcon());
            existingSeller.get().setCompanyName(sellerDto.getCompanyName());
            sellerRepository.save(existingSeller.get());
        }else{
            throw new UsernameNotFoundException("User not found with email: " + sellerDto.getEmail());
        }
    }

    @Override
    public void deleteSeller(String sellerId) {
        sellerRepository.deleteById(sellerId);
    }

    @Override
    public void findSellerPassword(String sellerId) {
        // find password logic
    }

    private Seller convertToSeller(SellerDto sellerDto) {
        if (sellerDto == null) {
            return null;
        }

        Seller seller = new Seller();
        seller.setSellerId(sellerDto.getSellerId());
        seller.setEmail(sellerDto.getEmail());
        return seller;
    }

}
