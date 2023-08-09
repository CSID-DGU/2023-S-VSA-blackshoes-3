package com.travelvcommerce.personalizedservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VideoDto {

    @Data
    public static class LikeVideoResponseDto implements FormattedCreatedAt {
        private String userId;
        private LocalDateTime createdAt;
    }

    @Data
    public static class GetLikedVideosResponseDto {
        private String userId;
        private List<String> videoIdList;
    }


    @Data
    public static class ViewVideoResponseDto implements FormattedCreatedAt {
        private LocalDateTime createdAt;
        private Long videoViewCount;
        private LocalDateTime updatedAt;
        private String userId;
        private String videoId;

        public String getFormattedUpdatedAt() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            return getUpdatedAt().format(formatter);
        }
    }
}
