package com.travelvcommerce.personalizedservice.service;

import com.travelvcommerce.personalizedservice.dto.ResponseDto;
import com.travelvcommerce.personalizedservice.dto.VideoDto;

import java.util.List;
import java.util.Map;

public interface VideoService {

    /**
     * sellerId로 시청기록을 삭제한다.
     * @param sellerId
     */
    void deleteHistoryBySellerId(String sellerId);

    /**
     * userId로 시청기록을 삭제한다.
     * @param userId
     */
    void deleteHistoryByUserId(String userId);

    /**
     * videoId로 조회기록을 삭제한다.
     * @param videoId
     */
    void deleteHistoryByVideoId(String videoId);

    /**
     * 비디오 조회, 이미 조회한 경우 count를 1 증가시킨다.
     * @param videoId
     */
    Map<String, String> viewVideo(String userId, String videoId, String sellerId);

    Map<String, String> unviewVideo(String userId, String videoId);

    List<String> getViewVideoIdList(String userId);

    void deleteLikeVideoByVideoId(String videoId);

    void deleteLikeVideoBySellerId(String sellerId);

    void deleteLikeVideoByUserId(String userId);
    /**
     * 비디오를 좋아요 한다.
     * @param videoId
     */
    Map<String, String> likeVideo(String userId, String videoId, String sellerId);

    /**
     * 비디오 좋아요를 해제한다.
     * @param videoId
     */
    Map<String, String> unlikeVideo(String userId, String videoId);

    /**
     * 사용자가 좋아요한 비디오 리스트를 반환한다.
     * @param userId
     * @return 좋아요한 비디오 리스트
     */
    List<String> getLikedVideoIdList(String userId);

}
