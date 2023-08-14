package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.dto.RankDto;
import com.travelvcommerce.statisticsservice.dto.TagRankDto;
import com.travelvcommerce.statisticsservice.dto.count.TotalTagViewCountDto;
import com.travelvcommerce.statisticsservice.entity.TagViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagViewCountRepository extends JpaRepository<TagViewCount, Long> {
    @Query("DELETE FROM TagViewCount tvc WHERE tvc.video.videoId IN :videoIds AND tvc.tag.tagId IN :tagIds")
    void deleteByVideoIdAndTagId(String videoId, String tagId);

    @Query("SELECT new com.travelvcommerce.statisticsservice.dto.count.TotalTagViewCountDto(tvc.tag, SUM(tvc.viewCount)) " +
            "FROM TagViewCount tvc " +
            "WHERE tvc.video.sellerId = :sellerId " +
            "GROUP BY tvc.tag " +
            "ORDER BY SUM(tvc.viewCount) DESC")
    List<TotalTagViewCountDto> findTop10BySellerIdOrderByViewCountDesc(String sellerId);

    @Query("SELECT new com.travelvcommerce.statisticsservice.dto.TagRankDto(tvc.tag.tagId, tvc.tag.content, tvc.tag.type)" +
            "FROM TagViewCount tvc " +
            "WHERE tvc.tag.type = 'region'" +
            "GROUP BY tvc.tag " +
            "ORDER BY SUM(tvc.viewCount) DESC")
    List<TagRankDto> findRegionTop10();

    @Query("SELECT new com.travelvcommerce.statisticsservice.dto.TagRankDto(tvc.tag.tagId, tvc.tag.content, tvc.tag.type)" +
            "FROM TagViewCount tvc " +
            "WHERE tvc.tag.type = 'theme'" +
            "GROUP BY tvc.tag " +
            "ORDER BY SUM(tvc.viewCount) DESC")
    List<TagRankDto> findThemeTop10();
}
