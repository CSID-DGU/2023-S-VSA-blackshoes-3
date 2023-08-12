package com.tavelvcommerce.commentservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {
    private String commentId;
    private String videoId;
    private String userId;
    private String content;
    private String createdAt;
    private String updatedAt;

    @Getter
    public static class CommentRequestDto {
        private String content;
    }

    @Getter
    @Builder
    public static class CommentCreateResponseDto {
        private String commentId;
        private String createdAt;
    }

    @Getter
    @Builder
    public static class CommentUpdateResponseDto {
        private String commentId;
        private String updatedAt;
    }
}
