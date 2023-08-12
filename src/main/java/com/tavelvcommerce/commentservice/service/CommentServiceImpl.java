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

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDto.CommentCreateResponseDto createComment(String commentId, String videoId, String userId, String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content is empty");
        }

        if (content.length() > 140) {
            throw new IllegalArgumentException("Content should not exceed 140 characters");
        }

        Comment comment = Comment.builder()
                .commentId(commentId)
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
}
