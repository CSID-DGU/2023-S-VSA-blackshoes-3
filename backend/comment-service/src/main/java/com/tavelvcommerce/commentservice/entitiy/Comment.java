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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "comment_id")
    private String commentId;

    @JoinColumn(name = "video_id")
    @ManyToOne
    private Video video;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = true)
    private Timestamp updatedAt;

    @Builder
    public Comment(String commentId, Video video, User user, String content, Timestamp createdAt, Timestamp updatedAt) {
        this.commentId = commentId;
        this.video = video;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
