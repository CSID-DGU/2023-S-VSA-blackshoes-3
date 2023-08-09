package com.travelvcommerce.personalizedservice.repository;

import com.travelvcommerce.personalizedservice.entity.ViewTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewTagRepository extends JpaRepository<ViewTag, String> {
    public boolean existsByUserIdAndTagId(String userId, String tagId);
    public ViewTag findByUserIdAndTagId(String userId, String tagId);
}
