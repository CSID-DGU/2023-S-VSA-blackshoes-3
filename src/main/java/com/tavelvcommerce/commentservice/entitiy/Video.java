package com.tavelvcommerce.commentservice.entitiy;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "videos", uniqueConstraints = @UniqueConstraint(name = "video_id_unique", columnNames = {"video_id"}))
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Builder
    public Video(String videoId, String sellerId) {
        this.videoId = videoId;
        this.sellerId = sellerId;
    }
}
