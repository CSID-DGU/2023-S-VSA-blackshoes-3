package com.travelvcommerce.uploadservice.entity;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ad_id;

    private String ad_url;

    private String ad_content;

    private String start_time;

    private String  end_time;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    private Video video;
}
