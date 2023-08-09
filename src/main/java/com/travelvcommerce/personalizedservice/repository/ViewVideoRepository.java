package com.travelvcommerce.personalizedservice.repository;

import com.travelvcommerce.personalizedservice.entity.ViewVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ViewVideoRepository extends JpaRepository<ViewVideo, String> {
    public ViewVideo findByUserIdAndVideoId(String userId, String videoId);
    public void deleteByUserIdAndVideoId(String userId, String videoId);
    public void deleteBySellerId(String sellerId);
    public void deleteByUserId(String userId);
    public void deleteByVideoId(String videoId);
    public boolean existsByUserIdAndVideoId(String userId, String videoId);

    boolean existsBySellerId(String sellerId);

    boolean existsByUserId(String userId);

    boolean existsByVideoId(String videoId);

    List<ViewVideo> findByUserId(String userId);
}
