package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.TagViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagViewCountRepository extends JpaRepository<TagViewCount, Long> {
    Collection<TagViewCount> findAllByVideoIdAndSellerId(String videoId, String sellerId);

    void deleteByVideoIdAndTagId(String videoId, String tagId);

    void deleteAllByVideoId(String videoId);

    Collection<TagViewCount> findAllByVideoId(String videoId);

    @Query("SELECT tvc " +
            "FROM TagViewCount tvc " +
            "WHERE tvc.sellerId = :sellerId " +
            "GROUP BY tvc.tagId " +
            "ORDER BY SUM(tvc.viewCount) DESC")
    List<TagViewCount> findTop10BySellerIdOrderByCountDesc(String sellerId);

    Optional<TagViewCount> findByVideoIdAndTagId(String videoId, String tagId);
}
