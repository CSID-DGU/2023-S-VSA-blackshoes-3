package com.travelvcommerce.uploadservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DenormalizedVideoDto implements Serializable {
    private String videoId;
    private String videoName;
    private String videoUrl;
    private String thumbnailUrl;
    private String sellerId;
    private String sellerName;
    private String sellerLogo;
    private String createdAt;
    private String updatedAt;
    private List<DenormalizedTagDto> videoTags;
    private List<DenormalizedAdDto> videoAds;
    private int likes;
    private int views;
    private int adClicks;
}
