package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.AdDto;
import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.entity.VideoUrl;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;

import java.util.List;

public interface VideoModifyService {
    public Video getVideo(String userId, String videoId);
    public String getThumbnailS3Key(Video video);
    public void updateThumbnail(Video video, VideoUrl videoUrl, S3Thumbnail s3Thumbnail);
    public void updateTags(String userId, String videoId, List<String> tagIdList);
    public void updateAds(String userId, String videoId, List<AdDto.AdModifyRequestDto> adModifyRequestDtoList);
}
