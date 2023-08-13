package com.travelvcommerce.statisticsservice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "user_id")
    private String userId;

    @Builder
    public Like(String videoId, String userId) {
        this.videoId = videoId;
        this.userId = userId;
    }
}
