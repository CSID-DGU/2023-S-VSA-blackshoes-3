package com.travelvcommerce.uploadservice.repository;

import com.travelvcommerce.uploadservice.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    Optional<Ad> findByAdId(String adId);
}
