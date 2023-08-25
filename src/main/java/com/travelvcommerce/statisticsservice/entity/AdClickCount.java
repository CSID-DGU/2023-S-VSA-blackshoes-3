package com.travelvcommerce.statisticsservice.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Table(name = "ad_click_counts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdClickCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ad_id")
    private String adId;

    @JoinColumn(name = "video_id", referencedColumnName = "video_id", foreignKey = @ForeignKey(name = "ad_click_count_fk_video_id"))
    @ManyToOne
    private Video video;

    @Column(name = "click_count")
    @ColumnDefault("0")
    private long clickCount;

    @Builder
    public AdClickCount(String adId, Video video, long clickCount) {
        this.adId = adId;
        this.video = video;
        this.clickCount = clickCount;
    }

    public void increaseClickCount() {
        this.clickCount++;
    }

    public String getVideoId() {
        return this.video.getVideoId();
    }

    public String getVideoName() {
        return this.video.getVideoName();
    }
}
