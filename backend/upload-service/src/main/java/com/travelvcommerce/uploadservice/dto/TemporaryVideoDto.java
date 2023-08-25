package com.travelvcommerce.uploadservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TemporaryVideoDto {
    private String videoId;
    private String sellerId;
    private String videoS3Url;
    private String videoCloudfrontUrl;
    private String uploadedAt;
    private String expiredAt;

    @Data
    public static class TemporaryVideoResponseDto {
        private String videoId;
        private String videoCloudfrontUrl;
        private String uploadedAt;
        private String expiredAt;
    }
}
