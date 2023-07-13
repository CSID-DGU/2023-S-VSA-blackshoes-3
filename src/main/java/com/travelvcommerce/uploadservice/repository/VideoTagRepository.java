package com.travelvcommerce.uploadservice.repository;

import com.travelvcommerce.uploadservice.entity.VideoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoTagRepository extends JpaRepository<VideoTag, Long> {
}
