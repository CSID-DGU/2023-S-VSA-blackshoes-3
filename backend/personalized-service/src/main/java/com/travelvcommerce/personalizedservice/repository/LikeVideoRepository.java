package com.travelvcommerce.personalizedservice.repository;

import com.travelvcommerce.personalizedservice.entity.LikeVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface LikeVideoRepository extends JpaRepository<LikeVideo, String> {
    public LikeVideo findByUserIdAndVideoId(String userId, String videoId);
    public void deleteByUserIdAndVideoId(String userId, String videoId);
    public void deleteBySellerId(String sellerId);
    public void deleteByUserId(String userId);
    public void deleteByVideoId(String videoId);
    public boolean existsByUserIdAndVideoId(String userId, String videoId);
    boolean existsByUserId(String userId);
    List<LikeVideo> findByUserId(String userId);

    Page<LikeVideo> findByUserId(String userId, Pageable pageable);
}
