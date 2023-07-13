package com.travelvcommerce.uploadservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDto {
    private Long id;
    private String tagId;
    private String type;
    private String content;

    @Data
    public static class TagResponseDto {
        private String tagId;
        private String type;
        private String content;
    }
}
