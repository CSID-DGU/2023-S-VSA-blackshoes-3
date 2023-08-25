package com.travelvcommerce.userservice.dto;


import lombok.Data;

@Data
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    @Data
    public static class RefreshTokenDto {
        private String refreshToken;

        public RefreshTokenDto(String refreshToken, String email) {
            this.refreshToken = refreshToken;
        }
    }
}

