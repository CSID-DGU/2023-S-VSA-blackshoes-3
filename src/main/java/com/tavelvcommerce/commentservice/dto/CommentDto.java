package com.tavelvcommerce.commentservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

public class CommentDto {
    @Getter
    public static class CommentRequestDto {
        private String userId;
        private String nickname;
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

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CommentResponseDto {
        private String commentId;
        private String sellerId;
        private String videoId;
        private String userId;
        private String nickname;
        private String content;
        private Timestamp createdAt;
        private Timestamp updatedAt;
    }
}
