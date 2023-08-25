package com.travelvcommerce.userservice.dto;

import lombok.Data;

@Data
public class EmailDto {
    private String email;
    private String title;
    private String message;

    public EmailDto(String email, String title, String message) {
        this.email = email;
        this.title = title;
        this.message = message;
    }

    @Data
    public static class EmailRequestDto {
        private String email;
    }

    @Data
    public static class EmailVerificationRequestDto {
        private String email;
        private String verificationCode;
    }
}
