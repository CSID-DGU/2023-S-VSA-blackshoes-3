package com.travelvcommerce.uploadservice.repository;

import com.travelvcommerce.uploadservice.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByType(String type);

    Optional<Object> findByTagId(String tagId);
}
