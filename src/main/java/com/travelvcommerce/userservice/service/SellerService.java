package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.SellerDto;
import com.travelvcommerce.userservice.dto.TokenDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Transactional
@Service
public interface SellerService {
    void updateSeller(String sellerId, SellerDto sellerDto);
    void deleteSeller(String sellerId);
    void findSellerPassword(String sellerId);
    void registerSeller(SellerDto.SellerRegisterRequestDto sellerRegisterRequestDto);
    Map<String, String> login(SellerDto.SellerLoginRequestDto sellerLoginRequestDto);
}
