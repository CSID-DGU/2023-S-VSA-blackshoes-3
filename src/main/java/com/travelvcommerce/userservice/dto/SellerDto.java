package com.travelvcommerce.userservice.dto;

import lombok.Data;

@Data
public class SellerDto {
    private String email;
    private String password;
    private String companyName;
    private byte[] icon;
    private String sellerId;


    @Data
    public static class SellerRegisterRequestDto {
        private String email;
        private String password;
        private String companyName;
        private byte[] icon;
    }
    @Data
    public static class SellerLoginRequestDto {
        private String email;
        private String password;
    }
}
