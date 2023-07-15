package com.travelvcommerce.contentslaveservice.repository;

import com.travelvcommerce.contentslaveservice.entity.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
}
