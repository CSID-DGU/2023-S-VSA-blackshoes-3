package com.tavelvcommerce.commentservice.repository;

import com.tavelvcommerce.commentservice.dto.CommentDto;
import com.tavelvcommerce.commentservice.entitiy.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.video.videoId = :videoId AND c.video.sellerId = :sellerId")
    Page<Comment> findCommentsByVideoIdAndSellerId(String videoId, String sellerId, Pageable pageable);

    Page<Comment> findCommentsByUserId(String userId, Pageable pageable);

    List<Comment> findAllByUserId(String userId);

    void deleteAllByUserId(String userId);

    Optional<Comment> findCommentByCommentIdAndUserId(String commentId, String userId);

    Optional<Comment> findCommentByCommentId(String commentId);

    @Query("SELECT c FROM Comment c WHERE c.video.videoId = :videoId")
    Page<Comment> findCommentsByVideoId(String videoId, Pageable pageable);
}
