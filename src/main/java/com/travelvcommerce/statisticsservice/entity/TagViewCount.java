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

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tag_id", foreignKey = @ForeignKey(name = "tag_view_count_fk_tag_id"))
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "video_id", foreignKey = @ForeignKey(name = "tag_view_count_fk_video_id"))
    private Video video;

    @Column(name = "view_count")
    private long viewCount;

    @Builder
    public TagViewCount(Tag tag, Video video, long viewCount) {
        this.tag = tag;
        this.video = video;
        this.viewCount = viewCount;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public String getTagId() {
        return this.tag.getTagId();
    }
}
