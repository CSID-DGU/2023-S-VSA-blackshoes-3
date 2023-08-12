package com.tavelvcommerce.commentservice.repository;

import com.tavelvcommerce.commentservice.entitiy.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentIdAndVideoIdAndUserId(String commentId, String videoId, String userId);
}
