package com.travelvcommerce.uploadservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.travelvcommerce.uploadservice.entity.Uploader;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
