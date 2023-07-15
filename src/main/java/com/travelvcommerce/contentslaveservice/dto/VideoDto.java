package com.travelvcommerce.contentslaveservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class VideoDto {
    private String id;
    private String videoId;
    private String videoName;
    private String videoUrl;
    private String thumbnailUrl;
    private String sellerId;
    private String sellerName;
    private String sellerLogoUrl;
    private String createdAt;
    private List<Map<String, String>> videoTags;
    private List<Map<String, Map<String, String>>> videoAds;
    private int likes;
    private int views;

    @Data
    public static class VideoListResponseDto {
        private String videoId;
        private String videoName;
        private String thumbnailUrl;
        private String sellerName;
        private String sellerLogoUrl;
        private String createdAt;
        private int likes;
        private int views;
        private int adClicks;
    }

    @Data
    public static class VideoDetailResponseDto {
        private String videoId;
        private String videoName;
        private String videoUrl;
        private String thumbnailUrl;
        private String sellerId;
        private String sellerName;
        private String sellerLogoUrl;
        private String createdAt;
        private List<Map<String, String>> videoTags;
        private List<Map<String, Map<String, String>>> videoAds;
        private int likes;
        private int views;
        private int adClicks;
    }
}
