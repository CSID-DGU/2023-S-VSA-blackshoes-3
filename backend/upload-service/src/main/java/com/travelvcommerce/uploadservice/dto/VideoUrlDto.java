package com.travelvcommerce.uploadservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoUrlDto {
    private Long id;

    private String videoId;

    private String videoS3Url;

    private String videoCloudfrontUrl;

    private String thumbnailS3Url;

    private String thumbnailCloudfrontUrl;
}
