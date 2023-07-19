package com.travelvcommerce.uploadservice.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "videos",
        uniqueConstraints = @UniqueConstraint(name = "video_unique",
                columnNames = {"video_id", "video_name"}))
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "video_name", nullable = false, length = 100)
    private String videoName;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @Column(name = "seller_name", nullable = false)
    private String sellerName;

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
}
