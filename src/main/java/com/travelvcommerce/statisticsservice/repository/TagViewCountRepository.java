package com.travelvcommerce.statisticsservice.repository;

import com.travelvcommerce.statisticsservice.entity.TagViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagViewCountRepository extends JpaRepository<TagViewCount, Long> {

}
