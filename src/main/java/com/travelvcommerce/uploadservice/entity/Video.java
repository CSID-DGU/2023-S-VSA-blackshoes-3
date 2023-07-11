package com.travelvcommerce.uploadservice.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "video_name")
    private String videoName;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<VideoTag> videoTags;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Ad> ads;
}
