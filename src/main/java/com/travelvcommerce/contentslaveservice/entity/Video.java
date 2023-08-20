package com.travelvcommerce.contentslaveservice.entity;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "videos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video {
    @Id
    private String _id;
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

    @Builder
    public Video(String videoId, String videoName, String videoUrl, String thumbnailUrl, String sellerId, String sellerName, String sellerLogo, String createdAt, List<Map<String, String>> videoTags, List<Map<String, String>> videoAds, long likes, long views, long adClicks) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerLogo = sellerLogo;
        this.createdAt = createdAt;
        this.videoTags = videoTags;
        this.videoAds = videoAds;
        this.likes = likes;
        this.views = views;
        this.adClicks = adClicks;
    }

    public void update(VideoDto videoDto) {
        if (videoDto.getVideoName() != null) {
            this.videoName = videoDto.getVideoName();
        }
        if (videoDto.getVideoUrl() != null) {
            this.videoUrl = videoDto.getVideoUrl();
        }
        if (videoDto.getThumbnailUrl() != null) {
            this.thumbnailUrl = videoDto.getThumbnailUrl();
        }
        if (videoDto.getSellerId() != null) {
            this.sellerId = videoDto.getSellerId();
        }
        if (videoDto.getSellerName() != null) {
            this.sellerName = videoDto.getSellerName();
        }
        if (videoDto.getSellerLogo() != null) {
            this.sellerLogo = videoDto.getSellerLogo();
        }
        if (videoDto.getVideoTags() != null) {
            this.videoTags = videoDto.getVideoTags();
        }
        if (videoDto.getVideoAds() != null) {
            this.videoAds = videoDto.getVideoAds();
        }
        if (videoDto.getLikes() != 0) {
            this.likes = videoDto.getLikes();
        }
        if (videoDto.getViews() != 0) {
            this.views = videoDto.getViews();
        }
        if (videoDto.getAdClicks() != 0) {
            this.adClicks = videoDto.getAdClicks();
        }
    }

    public void updateLikes(long likes) {
        this.likes = likes;
    }

    public void updateViews(long views) {
        this.views = views;
    }

    public void updateAdClicks(long adClicks) {
        this.adClicks = adClicks;
    }
}
