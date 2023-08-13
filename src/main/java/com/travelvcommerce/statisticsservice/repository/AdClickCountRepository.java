package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.AdClickCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdClickCountRepository extends JpaRepository<AdClickCount, Long> {
    Collection<AdClickCount> findAllByVideoId(String videoId);
    void deleteByVideoIdAndAdId(String videoId, String adId);

    void deleteAllByVideoId(String videoId);

    Optional<AdClickCount> findByAdId(String adId);
}
