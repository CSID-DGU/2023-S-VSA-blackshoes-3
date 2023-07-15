package com.travelvcommerce.contentslaveservice.repository;

import com.travelvcommerce.contentslaveservice.entity.Video;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface VideoRepository extends MongoRepository<Video, String> {
}
