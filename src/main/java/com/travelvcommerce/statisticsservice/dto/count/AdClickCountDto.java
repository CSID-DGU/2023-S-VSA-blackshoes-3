package com.travelvcommerce.statisticsservice.dto.count;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class AdClickCountDto {
    private String adId;
    private String videoId;
    private String videoName;
    private String adClicks;

    @Getter
    public static class AdClickRequestDto {
        private String userId;
    }

    @Builder
    @Getter
    public static class AdClickResponseDto  implements Serializable {
        private String adId;
        private String updatedAt;
    }
}
