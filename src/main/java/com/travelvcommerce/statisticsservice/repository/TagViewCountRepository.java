package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.TagViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TagViewCountRepository extends JpaRepository<TagViewCount, Long> {
    Collection<TagViewCount> findAllByVideoIdAndSellerId(String videoId, String sellerId);

    void deleteByVideoIdAndTagId(String videoId, String tagId);

    void deleteAllByVideoId(String videoId);
}
