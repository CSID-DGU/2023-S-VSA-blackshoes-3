package com.travelvcommerce.statisticsservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TagRankDto {
    private String tagId;
    private String content;
    private String type;

    @Builder
    public TagRankDto(String tagId, String content, String type) {
        this.tagId = tagId;
        this.content = content;
        this.type = type;
    }
}
