package com.travelvcommerce.uploadservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(name = "tag_content_unique", columnNames = {"content"}))
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @GeneratedValue(generator = "uuid")
    private String tag_id;
    @Column(length = 20, nullable = false)
    private String type;
    @Column(length = 20, nullable = false)
    private String content;
}
