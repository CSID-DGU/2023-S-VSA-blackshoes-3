package com.travelvcommerce.uploadservice.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Time;
import java.util.UUID;

@Data
@Entity
@Table(name = "ads",
        uniqueConstraints = @UniqueConstraint(name = "ad_unique",
                columnNames = {"ad_id", "ad_url", "ad_content"}))
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
    @JoinColumn(name = "video_id", referencedColumnName = "video_id", foreignKey = @ForeignKey(name = "ad_fk"))
    private Video video;
}
