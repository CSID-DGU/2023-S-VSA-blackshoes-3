package com.travelvcommerce.uploadservice.dto;

import lombok.Data;

@Data
public class AdDto {
    private Long id;

    private String adId;

    private String adUrl;

    private String adContent;

    private String startTime;

    private String endTime;

    @Data
    public static class AdRequestDto {
        private String adUrl;

        private String adContent;

        private String startTime;

        private String endTime;
    }
}
