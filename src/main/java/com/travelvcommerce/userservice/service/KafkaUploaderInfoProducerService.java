package com.travelvcommerce.userservice.service;

import com.travelvcommerce.userservice.dto.SellerDto;

public interface KafkaUploaderInfoProducerService {
    void createUploader(SellerDto.SellerInfoDto uploaderInfoDto);
    void updateUploader(SellerDto.SellerInfoDto uploaderInfoDto);
    void deleteUploader(String uploaderId);
}
