package com.travelvcommerce.uploadservice.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class VideoDto {
    private Long id;

    private String videoId;

    private String videoName;

    private String videoUrl;

    private String thumbnailUrl;

    private String sellerId;

    private String sellerName;

    private String createdAt;

    private String updatedAt;
}
