package com.travelvcommerce.uploadservice.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "temporary_videos", uniqueConstraints = @UniqueConstraint(name = "temporary_video_seller_unique", columnNames = {"seller_id"}))
public class TemporaryVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @Column(name = "video_s3_url", nullable = false)
    private String videoS3Url;

    @Column(name = "video_cloudfront_url", nullable = false)
    private String videoCloudfrontUrl;

    @Column(name = "uploaded_at", nullable = false)
    Timestamp uploadedAt;

    @Column(name = "expired_at", nullable = false)
    Timestamp expiredAt;

    @Builder
    public TemporaryVideo(String videoId, String sellerId, String videoS3Url, String videoCloudfrontUrl, Timestamp uploadedAt, Timestamp expiredAt) {
        this.videoId = videoId;
        this.sellerId = sellerId;
        this.videoS3Url = videoS3Url;
        this.videoCloudfrontUrl = videoCloudfrontUrl;
        this.uploadedAt = uploadedAt;
        this.expiredAt = expiredAt;
    }

    public void extendExpiredAt(Timestamp expiredAt) {
        this.expiredAt = expiredAt;
    }
}
