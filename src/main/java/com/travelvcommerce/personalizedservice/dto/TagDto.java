package com.travelvcommerce.personalizedservice.dto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class TagDto {

    @Data
    public static class InitTagListRequestDto {
        private List<String> tagIdList;
    }

    @Data
    public static class InitTagListResponseDto implements FormattedCreatedAt {
        private String userId;
        private LocalDateTime createdAt;
    }

    @Data
    public static class SubscribeTagRequestDto {
        private String tagId;
    }
    @Data
    public static class SubscribeTagResponseDto implements FormattedCreatedAt {
        private LocalDateTime createdAt;
    }
    @Data
    public static class UnsubscribeTagRequestDto {
        private String tagId;
    }


    @Data
    public static class ViewTagRequestDto {
        private List<String> tagIdList;
    }
}
