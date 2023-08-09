package com.travelvcommerce.personalizedservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Like_Videos")
public class LikeVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "seller_id")
    private String sellerId;

    @Builder
    public LikeVideo(String userId, String videoId, LocalDateTime createdAt, String sellerId) {
        this.userId = userId;
        this.videoId = videoId;
        this.createdAt = createdAt;
        this.sellerId = sellerId;
    }
}
/*
    * sellerId도 필요
    *
 */
