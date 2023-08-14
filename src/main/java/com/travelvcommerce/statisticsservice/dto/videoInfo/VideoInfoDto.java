package com.travelvcommerce.statisticsservice.dto.videoInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class VideoInfoDto implements Serializable {
    private String videoId;
    private String videoName;
    private String videoUrl;
    private String thumbnailUrl;
    private String sellerId;
    private String sellerName;
    private String sellerLogo;
    private String createdAt;
    private String updatedAt;
    private List<TagInfoDto> videoTags;
    private List<AdInfoDto> videoAds;

    @Data
    public static class VideoCreateDto implements Serializable {
        private String videoId;
        private String videoName;
        private String sellerId;
        private List<TagInfoDto> videoTags;
        private List<AdInfoDto> videoAds;
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
        private List<TagInfoDto> videoTags;
        private List<AdInfoDto> videoAds;
    }
}
