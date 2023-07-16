package com.travelvcommerce.contentslaveservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "videos")
@Data
public class Video {
    @Id
    private String _id;
    private String videoId;
    private String videoName;
    private String videoUrl;
    private String thumbnailUrl;
    private String sellerId;
    private String sellerName;
    private String sellerLogoUrl;
    private String createdAt;
    private List<Map<String, String>> videoTags;
    private List<Map<String, String>> videoAds;
    private int likes;
    private int views;
    private int adClicks;
}
