package com.travelvcommerce.contentslaveservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.travelvcommerce.contentslaveservice.entity.Video;
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
    private String sellerLogo;
    private String createdAt;
    private List<Map<String, String>> videoTags;
    private List<Map<String, String>> videoAds;
    private long likes;
    private long views;
    private long adClicks;

    public Video toEntity() {
        return Video.builder()
                .videoId(videoId)
                .videoName(videoName)
                .videoUrl(videoUrl)
                .thumbnailUrl(thumbnailUrl)
                .sellerId(sellerId)
                .sellerName(sellerName)
                .sellerLogo(sellerLogo)
                .createdAt(createdAt)
                .videoTags(videoTags)
                .videoAds(videoAds)
                .likes(likes)
                .views(views)
                .adClicks(adClicks)
                .build();
    }

    // 영상 리스트 조회 dto
    @Data
    public static class VideoListResponseDto {
        private String videoId;
        private String videoName;
        private String thumbnailUrl;
        private String sellerName;
        private String sellerLogo;
        private String createdAt;
        private long likes;
        private long views;
        private long adClicks;
    }

    // 개별 영상 조회 dto
    @Data
    public static class VideoDetailResponseDto {
        private String videoId;
        private String videoName;
        private String videoUrl;
        private String thumbnailUrl;
        private String sellerId;
        private String sellerName;
        private String sellerLogo;
        private String createdAt;
        private List<Map<String, String>> videoTags;
        private List<Map<String, String>> videoAds;
        private long likes;
        private long views;
        private long adClicks;
    }
}
