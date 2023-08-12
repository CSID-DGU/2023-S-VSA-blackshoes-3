package com.travelvcommerce.contentslaveservice.repository;

import com.travelvcommerce.contentslaveservice.dto.SearchAutoCompletionDto;
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
    // idType을 key로 갖는 value가 id와 일치하는 영상 조회
    @Query(value = "{?0: ?1}")
    Optional<VideoDto.VideoDetailResponseDto> findVideoById(String idType, String id);
    // 전체 영상 필요한 필드만 페이징 조회
    @Query(value = "{}", fields = "{videoId: 1, videoName: 1, thumbnailUrl: 1, sellerName: 1, sellerLogo: 1, createdAt: 1, likes: 1, views: 1, adClicks: 1}")
    Page<VideoDto.VideoListResponseDto> findVideosWithSelectedFields(Pageable pageable);
    // 판매자id 일치하는 영상 필요한 필드만 페이징 조회
    @Query(value = "{sellerId: ?0}", fields = "{videoId: 1, videoName: 1, thumbnailUrl: 1, sellerName: 1, sellerLogo: 1, createdAt: 1, likes: 1, views: 1, adClicks: 1}")
    Page<VideoDto.VideoListResponseDto> findVideosWithSellerIdAndSelectedFields(String sellerId, Pageable pageable);
    // idType을 key로 갖는 value가 idData에 포함되는 영상 필요한 필드만 페이징 조회
    @Query(value = "{?0: {$in: ?1}}", fields = "{videoId: 1, videoName: 1, thumbnailUrl: 1, sellerName: 1, sellerLogo: 1, createdAt: 1, likes: 1, views: 1, adClicks: 1}")
    Page<VideoDto.VideoListResponseDto> findVideosWithIdListAndSelectedFields(String idType, List<String> idData, Pageable pageable);

    @Query(value = "{videoId: ?0}")
    Optional<Video> findByByVideoId(String videoId);

    @Query(value = "{ 'videoName': { $regex: ?0 } }")
    Page<VideoDto.VideoListResponseDto> findVideosByVideoName(String videoName, Pageable pageable);

    @Query(value = "{ 'videoTags.tagName': { $regex: ?0 } }")
    Page<VideoDto.VideoListResponseDto> findVideosByTagName(String tagName, Pageable pageable);

    @Query(value = "{ 'sellerName': { $regex: ?0 } }")
    Page<VideoDto.VideoListResponseDto> findVideosBySellerName(String sellerName, Pageable pageable);

    @Query(value = "{'videoTags.tagId': ?0}")
    Page<VideoDto.VideoListResponseDto> findVideosByTagId(String q, Pageable pageable);

    @Query(value = "{videoId: {$in: ?0}}", fields = "{videoId: 1, videoName: 1, thumbnailUrl: 1, sellerName: 1, sellerLogo: 1, createdAt: 1, likes: 1, views: 1, adClicks: 1}")
    List<VideoDto.VideoListResponseDto> findVideosByVideoIdList(List<String> videoIdList);

    @Query(value = "{'videoName': { $regex: '?0', $options: 'i' }}", fields = "{videoName: 1}")
    List<SearchAutoCompletionDto> findAutoCompleteResultsByVideoName(String keyword);

    @Query(value = "{'sellerName': { $regex: '?0', $options: 'i' }}", fields = "{sellerName: 1}")
    List<SearchAutoCompletionDto> findAutoCompleteResultsBySellerName(String keyword);
}

