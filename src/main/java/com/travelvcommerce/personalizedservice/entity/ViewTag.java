package com.travelvcommerce.personalizedservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "View_Tags")
@Data
public class ViewTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_id")
    private String tagId;

    @Column(name = "user_id")
    private String userId;

    @Column(name="tag_view_count")
    private Long tagViewCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public ViewTag(String tagId, String userId, LocalDateTime createdAt, LocalDateTime updatedAt, Long tagViewCount) {
        this.tagId = tagId;
        this.userId = userId;
        this.tagViewCount = tagViewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
/*
 * 비디오 조회기록 삭제하더라도 태그 조회 기록은 남아있음.
 * TagId, ViewDate, UserId
 * MariaDB
 * */