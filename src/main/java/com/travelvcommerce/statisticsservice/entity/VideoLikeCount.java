package com.travelvcommerce.statisticsservice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "video_like_counts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoLikeCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "video_name")
    private String videoName;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "like_count")
    private long likeCount;

    @Builder
    public VideoLikeCount(String videoId, String videoName, String sellerId, long likeCount) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.sellerId = sellerId;
        this.likeCount = likeCount;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void updateVideoName(String videoName) {
        this.videoName = videoName;
    }
}
