package com.travelvcommerce.uploadservice.repository;

import com.travelvcommerce.uploadservice.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Object> findByVideoId(String videoId);
}
