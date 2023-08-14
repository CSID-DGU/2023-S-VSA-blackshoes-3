package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.VideoLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoLikeCountRepository extends JpaRepository<VideoLikeCount, Long> {
    @Query("SELECT vlc " +
            "FROM VideoLikeCount vlc " +
            "WHERE vlc.video.videoId IN :videoIds")
    Optional<VideoLikeCount> findByVideoId(String videoId);

    @Query("SELECT vlc " +
            "FROM VideoLikeCount vlc " +
            "WHERE vlc.video.sellerId = :sellerId " +
            "GROUP BY vlc.video " +
            "ORDER BY SUM(vlc.likeCount) DESC")
    List<VideoLikeCount> findTop10BySellerIdOrderByLikeCountDesc(String sellerId);
}
