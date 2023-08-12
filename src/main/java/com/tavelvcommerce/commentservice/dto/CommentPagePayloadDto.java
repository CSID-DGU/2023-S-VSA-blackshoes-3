package com.tavelvcommerce.commentservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommentPagePayloadDto {
    private int totalPages;
    private int currentPage;
    private boolean hasNext;
    private int pageSize;
    private long totalElements;
    private List<CommentDto.CommentResponseDto> comments;
}
