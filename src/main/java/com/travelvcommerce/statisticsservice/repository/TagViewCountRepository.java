package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.dto.TotalTagViewCountDto;
import com.travelvcommerce.statisticsservice.entity.TagViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagViewCountRepository extends JpaRepository<TagViewCount, Long> {
    @Query("DELETE FROM TagViewCount tvc WHERE tvc.video.videoId IN :videoIds AND tvc.tag.tagId IN :tagIds")
    void deleteByVideoIdAndTagId(String videoId, String tagId);

    @Query("SELECT new com.travelvcommerce.statisticsservice.dto.TotalTagViewCountDto(tvc.tag, SUM(tvc.viewCount)) " +
            "FROM TagViewCount tvc " +
            "WHERE tvc.video.sellerId = :sellerId " +
            "GROUP BY tvc.tag " +
            "ORDER BY SUM(tvc.viewCount) DESC")
    List<TotalTagViewCountDto> findTop10BySellerIdOrderByViewCountDesc(String sellerId);
}
