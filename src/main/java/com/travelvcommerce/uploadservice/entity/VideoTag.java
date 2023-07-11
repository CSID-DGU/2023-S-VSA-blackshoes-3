package com.travelvcommerce.uploadservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "video_tags")
public class VideoTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    private Video video;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tag_id")
    private Tag tag;
}
