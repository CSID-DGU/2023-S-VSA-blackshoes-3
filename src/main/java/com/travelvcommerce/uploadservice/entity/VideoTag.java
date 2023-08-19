package com.travelvcommerce.uploadservice.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "video_tags")
public class VideoTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "video_id", foreignKey = @ForeignKey(name = "video_tag_fk_video_id"))
    private Video video;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tag_id", foreignKey = @ForeignKey(name = "video_tag_fk_tag_id"))
    private Tag tag;

    @Builder
    public VideoTag(Video video, Tag tag) {
        this.video = video;
        this.tag = tag;
    }
}
