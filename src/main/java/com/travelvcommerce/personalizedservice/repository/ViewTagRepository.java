package com.travelvcommerce.personalizedservice.repository;

import com.travelvcommerce.personalizedservice.entity.ViewTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewTagRepository extends MongoRepository<ViewTag, String> {
    boolean existsByUserIdAndTagId(String userId, String tagId);
    ViewTag findByUserIdAndTagId(String userId, String tagId);
    boolean existsByUserId(String userId);
    List<ViewTag> findByUserId(String userId, Pageable pageable);
    Optional<ViewTag> findByUserId(String userId);
    void deleteByUserId(String userId);
}
