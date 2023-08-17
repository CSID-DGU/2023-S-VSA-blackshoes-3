package com.travelvcommerce.uploadservice.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "videos",
        uniqueConstraints = @UniqueConstraint(name = "video_unique",
                columnNames = {"video_id"}))
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "video_name", nullable = false, length = 100)
    private String videoName;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = true)
    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<VideoTag> videoTags;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Ad> ads;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_url_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "video_fk_video_url_id"))
    private VideoUrl videoUrl;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "seller_id", foreignKey = @ForeignKey(name = "video_fk_seller_id"))
    private Uploader uploader;
}
