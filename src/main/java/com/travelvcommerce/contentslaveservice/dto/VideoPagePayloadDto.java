package com.travelvcommerce.contentslaveservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VideoPagePayloadDto {
    private int totalPages;
    private int currentPage;
    private boolean hasNext;
    private int pageSize;
    private long totalElements;
    private List<VideoDto.VideoListResponseDto> videos;
}
