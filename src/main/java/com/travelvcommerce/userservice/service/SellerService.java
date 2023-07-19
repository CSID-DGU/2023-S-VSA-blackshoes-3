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
    void updatePassword(String sellerId, String password);
    void registerSeller(SellerDto.SellerRegisterRequestDto sellerRegisterRequestDto);
    Map<String, String> login(SellerDto.SellerLoginRequestDto sellerLoginRequestDto);
    SellerDto.SellerInfoDto getSellerInfo(String sellerId);
    SellerDto.SellerInfoDto getSellerUploaderInfo(String sellerId);
}
