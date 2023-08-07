package com.travelvcommerce.statisticsservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class VideoDto implements Serializable {
    private String videoId;
    private String videoName;
    private String videoUrl;
    private String thumbnailUrl;
    private String sellerId;
    private String sellerName;
    private String sellerLogo;
    private String createdAt;
    private String updatedAt;
    private List<TagDto> videoTags;
    private List<AdDto> videoAds;

    @Data
    public static class VideoCreateDto implements Serializable {
        private String videoId;
        private String sellerId;
        private List<TagDto> videoTags;
        private List<AdDto> videoAds;
    }

    @Data
    public static class VideoUpdateDto implements Serializable {
        private String videoId;
        private String videoName;
        private String videoUrl;
        private String thumbnailUrl;
        private String sellerId;
        private String sellerName;
        private String sellerLogo;
        private List<TagDto> videoTags;
        private List<AdDto> videoAds;
    }

    @Data
    public static class VideoLikesDto {
        private String videoId;
        private int likes;
    }

    @Data
    public static class VideoViewsDto {
        private String videoId;
        private int views;
    }

    @Data
    public static class VideoAdClicksDto {
        private String videoId;
        private int adClicks;
    }
}
