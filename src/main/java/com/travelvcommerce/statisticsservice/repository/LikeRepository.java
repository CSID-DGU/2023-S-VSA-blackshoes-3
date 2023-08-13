package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByVideoIdAndUserId(String videoId, String userId);

    void deleteAllByVideoId(String videoId);
}
