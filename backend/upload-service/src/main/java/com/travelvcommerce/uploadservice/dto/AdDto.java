package com.travelvcommerce.uploadservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdDto {
    private Long id;

    private String adId;

    private String adUrl;

    private String adContent;

    private String startTime;

    private String endTime;

    @Data
    public static class AdUploadRequestDto {
        private String adId;

        private String adUrl;

        private String adContent;

        private String startTime;

        private String endTime;
    }

    @Data
    public static class AdModifyRequestDto {
        private String modifyType;

        private String adId;

        private String adUrl;

        private String adContent;

        private String startTime;

        private String endTime;
    }

    @Data
    public static class AdModifyRequestsDto {
        private List<AdDto.AdModifyRequestDto> adModifyRequests;
    }
}
