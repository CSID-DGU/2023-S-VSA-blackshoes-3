package com.travelvcommerce.statisticsservice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name = "videos", uniqueConstraints = @UniqueConstraint(name = "video_id_unique", columnNames = {"video_id"}))
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @Column(name = "video_name", nullable = false)
    private String videoName;

    @OneToOne(mappedBy = "video", cascade = CascadeType.ALL)
    private VideoViewCount videoViewCount;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<TagViewCount> tagViewCounts;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToOne(mappedBy = "video", cascade = CascadeType.ALL)
    private VideoLikeCount videoLikeCount;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<AdClickCount> adClickCounts;

    @Builder
    public Video(String videoId, String sellerId, String videoName) {
        this.videoId = videoId;
        this.sellerId = sellerId;
        this.videoName = videoName;
    }

    public void updateVideoName(String videoName) {
        this.videoName = videoName;
    }
}
