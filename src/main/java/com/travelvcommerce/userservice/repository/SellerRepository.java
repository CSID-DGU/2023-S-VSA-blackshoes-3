package com.travelvcommerce.userservice.repository;

import com.travelvcommerce.userservice.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository  extends JpaRepository<Seller, String> {
    Optional<Seller> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Seller> findBySellerId(String sellerId);
}