package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.dto.count.TotalAdClickCountDto;
import com.travelvcommerce.statisticsservice.entity.AdClickCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdClickCountRepository extends JpaRepository<AdClickCount, Long> {

    Optional<AdClickCount> findByAdId(String adId);

    @Query("SELECT new com.travelvcommerce.statisticsservice.dto.count.TotalAdClickCountDto(acc.video, SUM(acc.clickCount)) " +
            "FROM AdClickCount acc " +
            "WHERE acc.video.sellerId = :sellerId " +
            "GROUP BY acc.video " +
            "ORDER BY SUM(acc.clickCount) DESC")
    List<TotalAdClickCountDto> findRankBySellerIdOrderByClickCountDesc(String sellerId, Pageable pageable);
}
