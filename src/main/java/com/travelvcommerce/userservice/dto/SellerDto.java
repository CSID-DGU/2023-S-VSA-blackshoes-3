package com.travelvcommerce.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
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

    @Data
    public static class SellerUpdateRequestDto{
        private String email;
        private String password;
        private String companyName;
    }

    @Data
    public static class SellerJoinRequestDto{
        private String email;
        private String password;
        private String companyName;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SellerInfoDto {
        private String email;
        private String companyName;
        private byte[] icon;
        private String sellerId;
    }
}
