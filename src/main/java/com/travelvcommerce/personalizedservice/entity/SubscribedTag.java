package com.travelvcommerce.personalizedservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Subscribed_Tags", uniqueConstraints = @UniqueConstraint(name = "tag_user_unique", columnNames = {"tag_id", "user_id"}))
public class SubscribedTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "tag_id")
    private String tagId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public SubscribedTag(String userId, String tagId, LocalDateTime createdAt) {
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