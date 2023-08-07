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

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "click_count")
    @ColumnDefault("0")
    private long clickCount;

    @Builder
    public AdClickCount(String adId, String videoId, String sellerId, long clickCount) {
        this.adId = adId;
        this.videoId = videoId;
        this.sellerId = sellerId;
        this.clickCount = clickCount;
    }

    public void increaseClickCount() {
        this.clickCount++;
    }
}
