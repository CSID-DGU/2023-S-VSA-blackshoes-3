package com.travelvcommerce.uploadservice.repository;

import com.travelvcommerce.uploadservice.entity.TemporaryVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryVideoRepository extends JpaRepository<TemporaryVideo, Long> {
    Optional<TemporaryVideo> findByVideoId(String videoId);

    Optional<TemporaryVideo> findBySellerIdAndVideoId(String sellerId, String videoId);
}
