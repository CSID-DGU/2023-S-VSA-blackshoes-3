package com.travelvcommerce.uploadservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "uploaders")
public class Uploader implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @Column(name = "seller_name", nullable = false)
    private String sellerName;

    @Column(name = "seller_logo", nullable = false, columnDefinition = "LONGBLOB")
    @Lob
    private byte[] sellerLogo;

    @OneToMany(mappedBy = "uploader", cascade = CascadeType.ALL)
    private List<Video> videos;
}
