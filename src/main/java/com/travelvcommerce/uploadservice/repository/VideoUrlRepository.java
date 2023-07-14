package com.travelvcommerce.uploadservice.repository;

import com.travelvcommerce.uploadservice.entity.VideoUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoUrlRepository extends JpaRepository<VideoUrl, Long> {
}
