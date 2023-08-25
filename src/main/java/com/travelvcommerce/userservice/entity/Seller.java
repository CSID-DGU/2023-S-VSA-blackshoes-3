package com.travelvcommerce.userservice.entity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Sellers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String sellerName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] sellerLogo;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "seller_id", unique = true)
    private String sellerId;

//    @PreUpdate
//    public void updatedAt() {
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PrePersist
//    public void createdDate() {
//        this.createdAt = LocalDateTime.now();
//    }

    public void updateSellerName(String sellerName) {
        this.sellerName = sellerName;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSellerLogo(byte[] sellerLogo) {
        this.sellerLogo = sellerLogo;
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public Seller(String email, String password, String sellerName, byte[] sellerLogo, String sellerId, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.email = email;
        this.password = password;
        this.sellerName = sellerName;
        this.sellerLogo = sellerLogo;
        this.sellerId = sellerId;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

