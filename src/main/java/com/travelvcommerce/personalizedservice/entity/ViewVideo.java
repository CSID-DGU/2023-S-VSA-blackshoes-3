package com.travelvcommerce.personalizedservice.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "View_Videos")
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
    private String createdAt;
    @Column(name = "seller_id")
    private String sellerId;

    @Builder
    public ViewVideo(String userId, String videoId, String createdAt, String sellerId) {
        this.userId = userId;
        this.videoId = videoId;
        this.createdAt = createdAt;
        this.sellerId = sellerId;
    }
}
/*
* VideoId, ViewDate, UserId, sellerId도 필요
* MariaDB
* */
