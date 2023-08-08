package com.travelvcommerce.personalizedservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "View_Tags")
public class ViewTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tagId")
    private Long tagId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "createdAt")
    private String createdAt;

}
/*
 * 비디오 조회기록 삭제하더라도 태그 조회 기록은 남아있음.
 * TagId, ViewDate, UserId
 * MariaDB
 * */