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

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "view_count")
    private long viewCount;

    @Builder
    public TagViewCount(String tagId, String sellerId, long viewCount) {
        this.tagId = tagId;
        this.sellerId = sellerId;
        this.viewCount = viewCount;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
