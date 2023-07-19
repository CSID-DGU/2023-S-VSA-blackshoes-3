package com.travelvcommerce.uploadservice.dto;

import lombok.Data;

@Data
public class UploaderDto {
    private Long id;
    private String sellerId;
    private String sellerName;
    private byte[] sellerLogo;

    @Data
    public static class UploaderResponseDto {
        private String sellerId;
        private String sellerName;
        private byte[] sellerLogo;
    }

    @Data
    public static class UploaderModifyRequestDto {
        private String sellerName;
        private byte[] sellerLogo;
    }
}
