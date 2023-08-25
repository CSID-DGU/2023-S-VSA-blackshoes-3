package com.travelvcommerce.statisticsservice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name = "video_view_counts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoViewCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "videoViewCount")
    private Video video;

    @Column(name = "view_count")
    private long viewCount;

    @Builder
    public VideoViewCount(Video video, long viewCount) {
        this.video = video;
        this.viewCount = viewCount;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public String getVideoId() {
        return this.video.getVideoId();
    }

    public String getVideoName() {
        return this.video.getVideoName();
    }
}
