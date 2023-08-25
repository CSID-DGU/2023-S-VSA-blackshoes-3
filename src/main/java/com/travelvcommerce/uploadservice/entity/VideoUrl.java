package com.travelvcommerce.uploadservice.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "video_urls")
public class VideoUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_s3_url", nullable = false)
    private String videoS3Url;

    @Column(name = "video_cloudfront_url", nullable = false)
    private String videoCloudfrontUrl;

    @Column(name = "thumbnail_s3_url", nullable = false)
    private String thumbnailS3Url;

    @Column(name = "thumbnail_cloudfront_url", nullable = false)
    private String thumbnailCloudfrontUrl;

    @Builder
    public VideoUrl(String videoS3Url, String videoCloudfrontUrl, String thumbnailS3Url, String thumbnailCloudfrontUrl) {
        this.videoS3Url = videoS3Url;
        this.videoCloudfrontUrl = videoCloudfrontUrl;
        this.thumbnailS3Url = thumbnailS3Url;
        this.thumbnailCloudfrontUrl = thumbnailCloudfrontUrl;
    }


    public void updateThumbnail(String thumbnailS3Url, String thumbnailCloudfrontUrl) {
        this.thumbnailS3Url = thumbnailS3Url;
        this.thumbnailCloudfrontUrl = thumbnailCloudfrontUrl;
    }
}
