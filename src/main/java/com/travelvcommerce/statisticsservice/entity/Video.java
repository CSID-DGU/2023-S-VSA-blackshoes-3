package com.travelvcommerce.statisticsservice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "videos", uniqueConstraints = @UniqueConstraint(name = "video_id_unique", columnNames = {"video_id"}))
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @Column(name = "video_name", nullable = false)
    private String videoName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_view_count_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "video_fk_video_view_count_id"))
    private VideoViewCount videoViewCount;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<TagViewCount> tagViewCounts;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_like_count_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "video_fk_video_like_count_id"))
    private VideoLikeCount videoLikeCount;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<AdClickCount> adClickCounts;

    @Builder
    public Video(String videoId, String sellerId, String videoName) {
        this.videoId = videoId;
        this.sellerId = sellerId;
        this.videoName = videoName;
    }

    public void setVideoViewCount(VideoViewCount videoViewCount) {
        this.videoViewCount = videoViewCount;
    }

    public void setVideoLikeCount(VideoLikeCount videoLikeCount) {
        this.videoLikeCount = videoLikeCount;
    }

    public void updateVideoName(String videoName) {
        this.videoName = videoName;
    }
}
