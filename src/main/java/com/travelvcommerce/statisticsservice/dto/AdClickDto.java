package com.travelvcommerce.statisticsservice.dto;

import lombok.Builder;
import lombok.Getter;

public class AdClickDto {
    @Getter
    public static class AdClickRequestDto {
        private String userId;
    }

    @Builder
    public static class AdClickResponseDto {
        private String adId;
        private String updatedAt;
    }
}
