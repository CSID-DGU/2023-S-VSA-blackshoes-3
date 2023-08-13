package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.VideoViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface VideoViewCountRepository extends JpaRepository<VideoViewCount, String> {
    void deleteAllByVideoId(String videoId);

    Optional<VideoViewCount> findByVideoId(String videoId);

    Collection<VideoViewCount> findAllByVideoId(String videoId);
}
