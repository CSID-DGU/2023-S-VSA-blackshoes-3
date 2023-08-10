package com.travelvcommerce.personalizedservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

}