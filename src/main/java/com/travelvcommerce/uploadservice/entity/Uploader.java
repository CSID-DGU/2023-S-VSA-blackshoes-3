package com.travelvcommerce.uploadservice.entity;

import com.travelvcommerce.uploadservice.dto.UploaderDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "uploaders", uniqueConstraints = @UniqueConstraint(name = "uploader_unique", columnNames = {"seller_id"}))
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

    public static void update(Uploader uploader, UploaderDto uploaderDto) {
        if (uploaderDto.getSellerName() != null) {
            uploader.setSellerId(uploaderDto.getSellerId());
        }
        if (uploaderDto.getSellerName() != null) {
            uploader.setSellerName(uploaderDto.getSellerName());
        }
        if (uploaderDto.getSellerLogo() != null) {
            uploader.setSellerLogo(uploaderDto.getSellerLogo());
        }
    }
}
