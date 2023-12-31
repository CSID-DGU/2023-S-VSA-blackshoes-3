package com.travelvcommerce.uploadservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class VideoDto {
    private Long id;

    private String videoId;

    private String videoName;

    private String sellerId;

    private String createdAt;

    private String updatedAt;

    @Data
    public static class VideoUploadRequestDto {
        private String videoName;

        private List<String> tagIdList;

        private List<AdDto.AdUploadRequestDto> adList;
    }

    @Data
    public static class VideoCreateResponseDto {
        private String videoId;
        private String createdAt;
    }

    @Data
    public static class VideoUpdateResponseDto {
        private String videoId;
        private String updatedAt;
    }

    @Data
    public static class VideoNameUpdateRequestDto {
        private String videoName;
    }
}
