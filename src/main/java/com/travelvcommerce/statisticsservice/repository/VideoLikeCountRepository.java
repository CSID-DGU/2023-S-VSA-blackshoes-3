package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.VideoLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoLikeCountRepository extends JpaRepository<VideoLikeCount, Long> {
    void deleteAllByVideoId(String videoId);
}
