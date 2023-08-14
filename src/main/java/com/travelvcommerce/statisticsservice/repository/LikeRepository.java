package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("SELECT l " +
            "FROM Like l " +
            "WHERE l.video.videoId = :videoId " +
            "AND l.userId = :userId")
    Optional<Like> findByVideoIdAndUserId(String videoId, String userId);
}
