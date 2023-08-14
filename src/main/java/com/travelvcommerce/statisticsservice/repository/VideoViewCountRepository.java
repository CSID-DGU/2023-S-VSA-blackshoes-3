package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.VideoViewCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoViewCountRepository extends JpaRepository<VideoViewCount, String> {
    @Query("SELECT vvc " +
            "FROM VideoViewCount vvc " +
            "WHERE vvc.video.sellerId = :sellerId " +
            "GROUP BY vvc.video " +
            "ORDER BY SUM(vvc.viewCount) DESC")
    List<VideoViewCount> findRankBySellerIdOrderByViewCountDesc(String sellerId, Pageable pageable);
}
