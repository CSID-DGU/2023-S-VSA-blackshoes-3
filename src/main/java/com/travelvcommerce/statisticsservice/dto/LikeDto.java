package com.travelvcommerce.statisticsservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

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
    @Getter
    public static class LikeResponseDto  implements Serializable {
        private String videoId;
        private String updatedAt;
    }
}
