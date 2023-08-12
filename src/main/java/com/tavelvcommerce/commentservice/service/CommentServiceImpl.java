package com.tavelvcommerce.commentservice.service;

import com.tavelvcommerce.commentservice.dto.CommentDto;
import com.tavelvcommerce.commentservice.entitiy.Comment;
import com.tavelvcommerce.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDto.CommentCreateResponseDto createComment(String commentId, String sellerId, String videoId, String userId, String content) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is empty");
        }

        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content is empty");
        }

        if (content.length() > 140) {
            throw new IllegalArgumentException("Content should not exceed 140 characters");
        }

        Comment comment = Comment.builder()
                .commentId(commentId)
                .sellerId(sellerId)
                .videoId(videoId)
                .userId(userId)
                .content(content)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            log.error("Failed to save comment: {}", e.getMessage());
            throw new RuntimeException("Failed to save comment");
        }

        CommentDto.CommentCreateResponseDto commentCreateResponseDto = CommentDto.CommentCreateResponseDto.builder()
                .commentId(comment.getCommentId())
                .createdAt(String.valueOf(comment.getCreatedAt()))
                .build();

        return commentCreateResponseDto;
    }

    @Override
    @Transactional
    public CommentDto.CommentUpdateResponseDto updateComment(String commentId, String videoId, String userId, String content) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is empty");
        }

        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content is empty");
        }

        if (content.length() > 140) {
            throw new IllegalArgumentException("Content should not exceed 140 characters");
        }

        Comment comment = commentRepository.findByCommentIdAndVideoIdAndUserId(commentId, videoId, userId).orElseThrow(() -> new NoSuchElementException("Comment not found"));

        try {
            comment.updateContent(content);
        } catch (Exception e) {
            log.error("Failed to update comment: {}", e.getMessage());
            throw new RuntimeException("Failed to update comment");
        }

        CommentDto.CommentUpdateResponseDto commentUpdateResponseDto = CommentDto.CommentUpdateResponseDto.builder()
                .commentId(comment.getCommentId())
                .updatedAt(String.valueOf(comment.getUpdatedAt()))
                .build();

        return commentUpdateResponseDto;
    }

    @Override
    public void deleteComment(String commentId, String videoId, String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is empty");
        }

        Comment comment = commentRepository.findByCommentIdAndVideoIdAndUserId(commentId, videoId, userId).orElseThrow(() -> new NoSuchElementException("Comment not found"));

        try {
            commentRepository.delete(comment);
        } catch (Exception e) {
            log.error("Failed to delete comment: {}", e.getMessage());
            throw new RuntimeException("Failed to delete comment");
        }
    }
}
