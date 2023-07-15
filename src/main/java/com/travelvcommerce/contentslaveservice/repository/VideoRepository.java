package com.travelvcommerce.contentslaveservice.repository;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
    Optional<VideoDto.VideoDetailResponseDto> findByVideoId(String videoId);
    @Query(value = "{}", fields = "{videoId: 1, videoName: 1, thumbnailUrl: 1, sellerName: 1, sellerLogoUrl: 1, createdAt: 1, likes: 1, views: 1, adClicks: 1}")
    Page<VideoDto.VideoListResponseDto> findVideosWithSelectedFields(Pageable pageable);
    @Query(value = "{sellerId: ?0}", fields = "{videoId: 1, videoName: 1, thumbnailUrl: 1, sellerName: 1, sellerLogoUrl: 1, createdAt: 1, likes: 1, views: 1, adClicks: 1}")
    Page<VideoDto.VideoListResponseDto> findVideosWithSellerIdAndSelectedFields(String sellerId, Pageable pageable);
    @Query(value = "{?0: {$in: ?1}}", fields = "{videoId: 1, videoName: 1, thumbnailUrl: 1, sellerName: 1, sellerLogoUrl: 1, createdAt: 1, likes: 1, views: 1, adClicks: 1}")
    Page<VideoDto.VideoListResponseDto> findVideosWithIdListAndSelectedFields(String idType, List<String> idData, Pageable pageable);
}
