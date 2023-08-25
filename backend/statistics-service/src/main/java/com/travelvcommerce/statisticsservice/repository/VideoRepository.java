package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByVideoId(String videoId);

    void deleteByVideoId(String videoId);
}
