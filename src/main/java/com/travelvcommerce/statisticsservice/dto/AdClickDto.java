package com.travelvcommerce.statisticsservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdClickDto {
    private String adId;
    private String videoId;
    private String videoName;
    private String adClicks;

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
