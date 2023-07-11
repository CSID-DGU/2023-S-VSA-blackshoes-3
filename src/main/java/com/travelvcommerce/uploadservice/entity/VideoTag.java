package com.travelvcommerce.uploadservice.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "video_tags")
public class VideoTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "video_id", foreignKey = @ForeignKey(name = "video_tag_fk_1"))
    private Video video;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tag_id", foreignKey = @ForeignKey(name = "video_tag_fk_2"))
    private Tag tag;
}
