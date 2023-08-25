package com.tavelvcommerce.commentservice.repository;

import com.tavelvcommerce.commentservice.entitiy.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    void deleteAllByVideoId(String videoId);

    Optional<Video> findByVideoId(String videoId);
}
