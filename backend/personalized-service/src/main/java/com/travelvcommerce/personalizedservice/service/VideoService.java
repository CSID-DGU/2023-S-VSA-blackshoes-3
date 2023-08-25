package com.travelvcommerce.personalizedservice.service;

import com.travelvcommerce.personalizedservice.dto.VideoDto;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface VideoService {
    Map<String, String> viewVideo(String userId, VideoDto.ViewVideoRequestDto viewVideoRequestDto);

    void unviewVideo(String userId, String videoId);

    Page<String> getViewVideoIdListWithViewCount(String userId, int page, int size);

    Map<String, String> likeVideo(String userId, VideoDto.LikeVideoRequestDto likeVideoRequestDto);

    void unlikeVideo(String userId, String videoId);
    Page<String> getLikedVideoIdList(String userId, int page, int size);

    Boolean isUserLikedVideo(String userId, String videoId);
}
