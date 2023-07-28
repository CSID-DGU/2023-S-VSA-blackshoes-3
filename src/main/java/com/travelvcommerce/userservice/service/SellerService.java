package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.SellerDto;
import com.travelvcommerce.userservice.dto.TokenDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Map;

@Transactional
@Service
public interface SellerService {
    Map<String, String> updateSeller(String sellerId, SellerDto.SellerUpdateRequestDto sellerUpdateRequestDto, MultipartFile sellerLogo);
    void deleteSeller(String sellerId);
    Map<String, String> updatePassword(String sellerId, String password);
    Map<String, String> registerSeller(SellerDto.SellerRegisterRequestDto sellerRegisterRequestDto);
    Map<String, String> login(SellerDto.SellerLoginRequestDto sellerLoginRequestDto);
    SellerDto.SellerInfoDto getSellerInfo(String sellerId);
    SellerDto.SellerInfoDto getSellerUploaderInfo(String sellerId);
}
