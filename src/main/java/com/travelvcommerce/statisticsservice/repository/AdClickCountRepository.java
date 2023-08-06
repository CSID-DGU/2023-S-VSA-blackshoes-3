package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.AdClickCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdClickCountRepository extends JpaRepository<AdClickCount, Long> {
}
