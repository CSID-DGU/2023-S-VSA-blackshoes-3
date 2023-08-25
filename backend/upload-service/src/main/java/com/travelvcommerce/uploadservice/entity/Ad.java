package com.travelvcommerce.uploadservice.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Time;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ads",
        uniqueConstraints = @UniqueConstraint(name = "ad_unique",
                columnNames = {"ad_id"}))
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad_id")
    private String adId;

    @Column(name = "ad_url", nullable = false)
    private String adUrl;

    @Column(name = "ad_content", nullable = false)
    private String adContent;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "video_id", foreignKey = @ForeignKey(name = "ad_fk_video_id"))
    private Video video;

    @Builder
    public Ad(String adId, String adUrl, String adContent, String startTime, String endTime) {
        this.adId = adId;
        this.adUrl = adUrl;
        this.adContent = adContent;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public void update(String adUrl, String adContent, String startTime, String endTime) {
        if (adUrl != this.adUrl) {
            this.adUrl = adUrl;
        }
        if (adContent != this.adContent) {
            this.adContent = adContent;
        }
        if (startTime != this.startTime) {
            this.startTime = startTime;
        }
        if (endTime != this.endTime) {
            this.endTime = endTime;
        }
    }
}
