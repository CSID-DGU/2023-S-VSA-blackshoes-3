package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByType(String type);

    Optional<Tag> findByTagId(String tagId);
}
