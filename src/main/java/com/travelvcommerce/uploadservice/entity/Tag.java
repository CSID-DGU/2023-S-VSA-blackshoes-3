package com.travelvcommerce.uploadservice.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(name = "tag_content_unique", columnNames = {"tag_id", "content"}))
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_id")
    private String tagId;
    @Column(length = 20, nullable = false)
    private String type;
    @Column(length = 20, nullable = false)
    private String content;
}
