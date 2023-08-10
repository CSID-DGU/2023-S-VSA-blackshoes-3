package com.travelvcommerce.personalizedservice.service;

import com.travelvcommerce.personalizedservice.dto.ResponseDto;
import com.travelvcommerce.personalizedservice.dto.VideoDto;

import java.util.List;
import java.util.Map;

public interface VideoService {
    Map<String, String> viewVideo(String userId, String videoId, String sellerId);
    Map<String, String> unviewVideo(String userId, String videoId);
    List<String> getViewVideoIdList(String userId);
    Map<String, String> likeVideo(String userId, String videoId, String sellerId);

    Map<String, String> unlikeVideo(String userId, String videoId);

    List<String> getLikedVideoIdList(String userId);

}
