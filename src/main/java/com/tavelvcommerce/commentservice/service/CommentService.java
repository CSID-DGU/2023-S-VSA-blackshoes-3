package com.tavelvcommerce.commentservice.service;

import com.tavelvcommerce.commentservice.dto.CommentDto;
import org.springframework.data.domain.Page;

public interface CommentService {
    CommentDto.CommentCreateResponseDto createComment(String commentId, String videoId, String userId, String content);

    CommentDto.CommentUpdateResponseDto updateComment(String commentId, String userId, String content);

    void userDeleteComment(String commentId, String userId);

    void sellerDeleteComment(String commentId, String sellerId);

    Page<CommentDto.CommentResponseDto> sellerVideoGetComments(String videoId, String sellerId, int page, int size);

    Page<CommentDto.CommentResponseDto> userVideoGetComments(String videoId, int page, int size);

    Page<CommentDto.CommentResponseDto> userGetComments(String userId, int page, int size);
}
