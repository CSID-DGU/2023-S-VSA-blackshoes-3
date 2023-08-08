package com.travelvcommerce.personalizedservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "View_Videos")
public class ViewVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "sellerId")
    private String sellerId;

    @Column(name = "videoId")
    private Long videoId;

    @Column(name = "createdAt")
    private String createdAt;

}
/*
* VideoId, ViewDate, UserId, sellerId도 필요
* MariaDB
* */
