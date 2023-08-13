package com.travelvcommerce.statisticsservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ViewCountDto {
    @Getter
    public static class ViewCountRequestDto {
        private String userId;
        private List<String> tagIdList;
    }

    @Builder
    public static class ViewCountResponseDto {
        private String videoId;
        private String updatedAt;
    }
}
