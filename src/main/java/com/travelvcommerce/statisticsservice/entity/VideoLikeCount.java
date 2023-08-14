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

    @JoinColumn(name = "video_id", referencedColumnName = "video_id", foreignKey = @ForeignKey(name = "video_like_count_fk_video_id"))
    @OneToOne
    private Video video;

    @Column(name = "like_count")
    private long likeCount;

    @Builder
    public VideoLikeCount(Video video, long likeCount) {
        this.video = video;
        this.likeCount = likeCount;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public String getVideoId() {
        return this.video.getVideoId();
    }

    public String getVideoName() {
        return this.video.getVideoName();
    }
}
