package com.tavelvcommerce.commentservice.service;

import com.tavelvcommerce.commentservice.dto.CommentDto;

public interface CommentService {
    CommentDto.CommentCreateResponseDto createComment(String commentId, String sellerId, String videoId, String userId, String content);

    CommentDto.CommentUpdateResponseDto updateComment(String commentId, String videoId, String userId, String content);

    void userDeleteComment(String commentId, String videoId, String userId);

    void sellerDeleteComment(String commentId, String videoId, String sellerId);
}
