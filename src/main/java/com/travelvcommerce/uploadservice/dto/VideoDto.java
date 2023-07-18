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

    private String sellerName;

    private String createdAt;

    private String updatedAt;

    @Data
    public static class VideoUploadRequestDto {
        private String videoName;

        private String sellerName;

        private List<String> tagIdList;

        private List<AdDto.AdUploadRequestDto> adList;
    }
}
