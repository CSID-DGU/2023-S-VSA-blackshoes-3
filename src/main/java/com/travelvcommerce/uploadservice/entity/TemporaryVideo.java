package com.travelvcommerce.uploadservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "temporary_videos")
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

}
