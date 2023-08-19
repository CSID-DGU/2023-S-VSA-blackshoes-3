package com.travelvcommerce.uploadservice.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(name = "tag_content_unique", columnNames = {"tag_id", "content"}))
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tag_id", unique = true)
    private String tagId;
    @Column(length = 20, nullable = false)
    private String type;
    @Column(length = 20, nullable = false)
    private String content;
}
