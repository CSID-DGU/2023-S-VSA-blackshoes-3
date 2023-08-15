package com.travelvcommerce.personalizedservice.repository;

import com.travelvcommerce.personalizedservice.entity.ViewTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewTagRepository extends JpaRepository<ViewTag, String> {
    boolean existsByUserIdAndTagId(String userId, String tagId);
    ViewTag findByUserIdAndTagId(String userId, String tagId);
    boolean existsByUserId(String userId);

    List<ViewTag> findByUserId(String userId, Pageable pageable);

    void deleteByUserId(String userId);
}
