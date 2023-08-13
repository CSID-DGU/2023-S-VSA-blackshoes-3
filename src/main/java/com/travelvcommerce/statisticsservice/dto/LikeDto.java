package com.travelvcommerce.statisticsservice.dto;

import lombok.Builder;
import lombok.Getter;

public class LikeDto {
    @Getter
    public static class LikeRequestDto {
        private String userId;
        private String action;
    }

    @Builder
    public static class LikeResponseDto {
        private String videoId;
        private String updatedAt;
    }
}
