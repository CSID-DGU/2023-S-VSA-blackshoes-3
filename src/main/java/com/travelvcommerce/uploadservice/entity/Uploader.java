package com.travelvcommerce.uploadservice.entity;

import com.travelvcommerce.uploadservice.dto.UploaderDto;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Uploader(String sellerId, String sellerName, byte[] sellerLogo) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerLogo = sellerLogo;
    }

    public void update(UploaderDto.UploaderModifyRequestDto uploaderModifyRequestDto) {
        if (uploaderModifyRequestDto.getSellerName() != null) {
           this.sellerName = uploaderModifyRequestDto.getSellerName();
        }
        if (uploaderModifyRequestDto.getSellerLogo() != null) {
            this.sellerLogo = uploaderModifyRequestDto.getSellerLogo();
        }
    }
}
