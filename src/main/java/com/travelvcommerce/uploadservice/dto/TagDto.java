package com.travelvcommerce.uploadservice.dto;

import lombok.Data;

@Data
public class TagDto {
    private Long id;
    private String tag_id;
    private String type;
    private String content;

    @Data
    public static class TagResponseDto {
        private String tag_id;
        private String type;
        private String content;
    }
}
