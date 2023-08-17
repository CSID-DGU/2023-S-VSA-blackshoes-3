package com.travelvcommerce.personalizedservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "View_Videos", uniqueConstraints = @UniqueConstraint(name = "view_video_unique", columnNames = {"video_id", "user_id"}))
@Data
public class ViewVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "video_view_count")
    private Long videoViewCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "seller_id")
    private String sellerId;

    public void increaseViewCount() {
        this.videoViewCount++;
    }

    @Builder
    public ViewVideo(String userId, String videoId, LocalDateTime createdAt, LocalDateTime updatedAt, String sellerId, Long videoViewCount) {
        this.userId = userId;
        this.videoId = videoId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sellerId = sellerId;
        this.videoViewCount = videoViewCount;
    }
}
/*
* VideoId, ViewDate, UserId, sellerId도 필요
* MariaDB
* */
