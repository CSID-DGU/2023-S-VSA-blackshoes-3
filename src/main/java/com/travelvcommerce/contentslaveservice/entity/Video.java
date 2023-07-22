package com.travelvcommerce.contentslaveservice.entity;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "videos")
@Data
@Builder
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
    private int likes;
    private int views;
    private int adClicks;

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
}
