package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.dto.RankDto;
import com.travelvcommerce.statisticsservice.dto.TagRankDto;
import com.travelvcommerce.statisticsservice.dto.count.TotalTagViewCountDto;
import com.travelvcommerce.statisticsservice.entity.TagViewCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagViewCountRepository extends JpaRepository<TagViewCount, Long> {
    @Query("SELECT new com.travelvcommerce.statisticsservice.dto.count.TotalTagViewCountDto(tvc.tag, SUM(tvc.viewCount)) " +
            "FROM TagViewCount tvc " +
            "WHERE tvc.video.sellerId = :sellerId " +
            "GROUP BY tvc.tag " +
            "ORDER BY SUM(tvc.viewCount) DESC")
    List<TotalTagViewCountDto> findRankBySellerIdOrderByViewCountDesc(String sellerId, Pageable pageable);

    @Query("SELECT new com.travelvcommerce.statisticsservice.dto.TagRankDto(tvc.tag.tagId, tvc.tag.content, tvc.tag.type)" +
            "FROM TagViewCount tvc " +
            "WHERE tvc.tag.type = 'region'" +
            "GROUP BY tvc.tag " +
            "ORDER BY SUM(tvc.viewCount) DESC")
    List<TagRankDto> findRegionRank(Pageable pageable);

    @Query("SELECT new com.travelvcommerce.statisticsservice.dto.TagRankDto(tvc.tag.tagId, tvc.tag.content, tvc.tag.type)" +
            "FROM TagViewCount tvc " +
            "WHERE tvc.tag.type = 'theme'" +
            "GROUP BY tvc.tag " +
            "ORDER BY SUM(tvc.viewCount) DESC")
    List<TagRankDto> findThemeRank(Pageable pageable);

    @Query("SELECT tvc " +
            "FROM TagViewCount tvc " +
            "WHERE tvc.video.videoId = :videoId AND tvc.tag.tagId = :tagId")
    Optional<TagViewCount> findByVideoIdAndTagId(String videoId, String tagId);
}
