package com.travelvcommerce.statisticsservice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "tag_view_counts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagViewCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "tag_id")
    private String tagId;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "video_name")
    private String videoName;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "view_count")
    private long viewCount;

    @Builder
    public TagViewCount(String tagId, String tagName, String videoId, String videoName, String sellerId, long viewCount) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.videoId = videoId;
        this.videoName = videoName;
        this.sellerId = sellerId;
        this.viewCount = viewCount;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void updateVideoName(String videoName) {
        this.videoName = videoName;
    }
}
