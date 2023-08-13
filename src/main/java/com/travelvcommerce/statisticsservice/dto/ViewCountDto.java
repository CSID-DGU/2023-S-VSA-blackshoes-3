package com.travelvcommerce.statisticsservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ViewCountDto {
    @Getter
    @Builder
    public static class videoViewCountDto {
          private String videoId;
          private String videoName;
          private String views;
    }

    @Getter
    @Builder
    public static class tagViewCountDto {
        private String tagId;
        private String tagName;
        private String views;
    }

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
