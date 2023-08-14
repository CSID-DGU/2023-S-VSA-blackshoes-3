package com.travelvcommerce.statisticsservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


public class RankResponseDto {
    @Getter
    @Builder
    public static class VideoViewRankResponseDto {
        private List<RankDto.VideoViewRankDto> videoViewRank;
        private String aggregatedAt;
    }

    @Getter
    @Builder
    public static class TagViewRankResponseDto {
        private List<RankDto.TagViewRankDto> tagViewRank;
        private String aggregatedAt;
    }

    @Getter
    @Builder
    public static class VideoLikeRankResponseDto {
        private List<RankDto.VideoLikeRankDto> videoLikeRank;
        private String aggregatedAt;
    }

    @Getter
    @Builder
    public static class VideoAdClickRankResponseDto {
        private List<RankDto.VideoAdClickRankDto> videoAdClickRank;
        private String aggregatedAt;
    }

    @Getter
    @Builder
    public static class TagRankResponseDto {
        private List<TagRankDto> tagRank;
        private String aggregatedAt;
    }
}
