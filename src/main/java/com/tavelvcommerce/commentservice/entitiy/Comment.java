package com.tavelvcommerce.commentservice.entitiy;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "comments")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "comment_id")
    private String commentId;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = true)
    private Timestamp updatedAt;

    @Builder
    public Comment(String commentId, String videoId, String userId, String content, Timestamp createdAt, Timestamp updatedAt) {
        this.commentId = commentId;
        this.videoId = videoId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
