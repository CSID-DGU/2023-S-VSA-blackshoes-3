package com.travelvcommerce.statisticsservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeDto {
    private String videoId;
    private String videoName;
    private String likes;

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
