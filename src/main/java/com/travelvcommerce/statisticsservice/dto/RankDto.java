package com.travelvcommerce.statisticsservice.dto;

import lombok.Builder;
import lombok.Getter;

public class RankDto {
    @Getter
    @Builder
    public static class TagViewRankDto {
        private String tagId;
        private String tagName;
        private long views;
    }

    @Getter
    @Builder
    public static class VideoLikeRankDto {
        private String videoId;
        private String videoName;
        private long likes;
    }

    @Getter
    @Builder
    public static class VideoViewRankDto {
        private String videoId;
        private String videoName;
        private long views;
    }

    @Getter
    @Builder
    public static class VideoAdClickRankDto {
        private String adId;
        private String videoId;
        private String videoName;
        private long adClicks;
    }
}
