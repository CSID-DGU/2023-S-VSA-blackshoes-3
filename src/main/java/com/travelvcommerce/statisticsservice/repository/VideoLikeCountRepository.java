package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.VideoLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoLikeCountRepository extends JpaRepository<VideoLikeCount, Long> {
    void deleteAllByVideoId(String videoId);

    Optional<VideoLikeCount> findByVideoId(String videoId);

    Collection<VideoLikeCount> findAllByVideoId(String videoId);

    List<VideoLikeCount> findTop10BySellerIdOrderByCountDesc(String sellerId);
}
