package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.AdDto;
import com.travelvcommerce.uploadservice.dto.UploaderDto;
import com.travelvcommerce.uploadservice.dto.VideoDto;
import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.entity.VideoUrl;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;

import java.util.List;

public interface VideoUpdateService {
    Video getVideo(String userId, String videoId);
    String getThumbnailS3Key(Video video);
    VideoDto.VideoUpdateResponseDto updateThumbnail(Video video, VideoUrl videoUrl, S3Thumbnail s3Thumbnail);
    VideoDto.VideoUpdateResponseDto updateTags(String userId, String videoId, List<String> tagIdList);
    VideoDto.VideoUpdateResponseDto updateAds(String userId, String videoId, List<AdDto.AdModifyRequestDto> adModifyRequestDtoList);
    void updateUploader(String userId, UploaderDto.UploaderModifyRequestDto uploaderModifyRequestDto);
}
