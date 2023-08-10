package com.travelvcommerce.personalizedservice.service;

import com.travelvcommerce.personalizedservice.dto.ResponseDto;
import com.travelvcommerce.personalizedservice.dto.VideoDto;

import java.util.List;
import java.util.Map;

public interface VideoService {

    Map<String, String> viewVideo(String userId, VideoDto.ViewVideoRequestDto viewVideoRequestDto);

    Map<String, String> unviewVideo(String userId, VideoDto.UnviewVideoRequestDto unviewVideoRequestDto);

    List<String> getViewVideoIdList(String userId);

    Map<String, String> likeVideo(String userId, VideoDto.LikeVideoRequestDto likeVideoRequestDto);

    void unlikeVideo(String userId, VideoDto.UnlikeVideoRequestDto unlikeVideoRequestDto);

    List<String> getLikedVideoIdList(String userId);

}
