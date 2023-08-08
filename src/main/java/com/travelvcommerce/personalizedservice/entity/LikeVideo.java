package com.travelvcommerce.personalizedservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "Like_Videos")
public class LikeVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "videoId")
    private Long videoId;

    @Column(name = "createdAt")
    private String createdAt;

    @Column(name = "sellerId")
    private String sellerId;

}
/*
    * sellerId도 필요
    *
 */
