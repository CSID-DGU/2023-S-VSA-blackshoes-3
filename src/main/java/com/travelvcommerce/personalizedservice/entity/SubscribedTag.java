package com.travelvcommerce.personalizedservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscribed_tags")
public class SubscribedTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private String userId;

    @Column(name = "tagId")
    private String tagId;

    @Column(name = "createdAt")
    private String createdAt;

    @Builder
    public SubscribedTag(String userId, String tagId, String createdAt, String updatedAt) {
        this.userId = userId;
        this.tagId = tagId;
        this.createdAt = createdAt;
    }
}

/*
* 1. post
* 2. delete
* 3. 초기 일괄 post
 */