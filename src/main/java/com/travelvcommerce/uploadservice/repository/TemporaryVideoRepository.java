package com.travelvcommerce.uploadservice.repository;

import com.travelvcommerce.uploadservice.entity.TemporaryVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TemporaryVideoRepository extends JpaRepository<TemporaryVideo, Long> {
    Optional<TemporaryVideo> findByVideoId(String videoId);

    Optional<TemporaryVideo> findBySellerIdAndVideoId(String sellerId, String videoId);

    Optional<TemporaryVideo> findBySellerId(String userId);

    @Query(value = "SELECT * FROM temporary_videos WHERE expired_at < ?1", nativeQuery = true)
    List<TemporaryVideo> findAllByExpiredVideo(Timestamp now);
}
