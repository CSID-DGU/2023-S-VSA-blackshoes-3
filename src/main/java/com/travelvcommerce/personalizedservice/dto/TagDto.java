package com.travelvcommerce.personalizedservice.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        private String tagId;
    }
    @Data
    public static class ViewTagResponseDto implements FormattedCreatedAt {
        private String userId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long tagViewCount;

        public String getFormattedUpdatedAt() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            return getUpdatedAt().format(formatter);
        }
    }
}
