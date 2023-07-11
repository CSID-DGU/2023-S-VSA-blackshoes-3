package com.travelvcommerce.uploadservice.repository;

import com.travelvcommerce.uploadservice.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}
