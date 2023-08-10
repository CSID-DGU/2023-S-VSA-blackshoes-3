package com.travelvcommerce.personalizedservice.repository;

import com.travelvcommerce.personalizedservice.entity.ViewTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewTagRepository extends JpaRepository<ViewTag, String> {
    boolean existsByUserIdAndTagId(String userId, String tagId);
    ViewTag findByUserIdAndTagId(String userId, String tagId);

    void deleteByUserId(String userId);
}
