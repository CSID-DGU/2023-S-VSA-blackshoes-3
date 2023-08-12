package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private GptRecommendationService gptRecommendationService;

    // 전체 영상 목록 조회, 정렬 정보 q로 받아서 정렬, 페이징 처리
    @Override
    public Page<VideoDto.VideoListResponseDto> getVideos(String sortType, int page, int size) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, sortType);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        Page<VideoDto.VideoListResponseDto> videoPage = videoRepository.findVideosWithSelectedFields(pageable);
        return videoPage;
    }

    // 판매자별 영상 목록 조회, 정렬 정보 q로 받아서 정렬, 페이징 처리
    @Override
    public Page<VideoDto.VideoListResponseDto> getVideosBySellerId(String sellerId, String q, int page, int size) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, q);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        Page<VideoDto.VideoListResponseDto> videoPage =
                videoRepository.findVideosWithSellerIdAndSelectedFields(sellerId, pageable);
        return videoPage;
    }

    // idType을 key로 갖는 value가 idData에 포함되는 영상 목록 조회, 정렬 정보 q로 받아서 정렬, 페이징 처리
    @Override
    public Page<VideoDto.VideoListResponseDto> getVideosByIdList(String idType, List<String> idData, String sortType, int page, int size) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, sortType);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        Page<VideoDto.VideoListResponseDto> videoPage =
                videoRepository.findVideosWithIdListAndSelectedFields(idType, idData, pageable);
        return videoPage;
    }

    // idType을 key로 갖는 value가 id와 일치하는 개별 영상 조회
    @Override
    public VideoDto.VideoDetailResponseDto getVideoById(String idType, String id) {
        VideoDto.VideoDetailResponseDto video;

        try {
            video =
                    videoRepository.findVideoById(idType, id)
                            .orElseThrow(() -> new RuntimeException("No video found"));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return video;
    }

    // 비디오 검색, 타입에 따라 쿼리 호출
    @Override
    public Page<VideoDto.VideoListResponseDto> searchVideos(String type, String query, String sortType, int page, int size) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, sortType);
        Pageable pageable = PageRequest.of(page, size, sortBy);

        switch(type) {
            case "videoName":
                return videoRepository.findVideosByVideoName(query, pageable);
            case "tagName":
                return videoRepository.findVideosByTagName(query, pageable);
            case "sellerName":
                return videoRepository.findVideosBySellerName(query, pageable);
            case "gpt":
                List<String> tagNameList = gptRecommendationService.getFiveRecommendedTag(query);
                return videoRepository.findVideosWithIdListAndSelectedFields("videoTags.tagName", tagNameList, pageable);
            default:
                throw new IllegalArgumentException("Invalid search type: " + type);
        }
    }

    // tagId에 해당하는 영상 목록 조회, q로 tagId 받아서 최신순 정렬, 페이징 처리
    @Override
    public Page<VideoDto.VideoListResponseDto> getVideosByTag(String q, String sortType, int page, int size) {
        Sort sortBy = Sort.by(Sort.Direction.DESC, sortType);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        Page<VideoDto.VideoListResponseDto> videoPage = videoRepository.findVideosByTagId(q, pageable);

        return videoPage;
    }

    @Override
    public List<VideoDto.VideoListResponseDto> getVideosByVideoIdList(List<String> videoIdList) {
        List<VideoDto.VideoListResponseDto> videoList = videoRepository.findVideosByVideoIdList(videoIdList);
        return videoList;
    }
}
