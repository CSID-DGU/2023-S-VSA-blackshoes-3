package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.AdDto;
import com.travelvcommerce.uploadservice.dto.DenormalizedVideoDto;
import com.travelvcommerce.uploadservice.dto.UploaderDto;
import com.travelvcommerce.uploadservice.dto.VideoDto;
import com.travelvcommerce.uploadservice.entity.Video;
import com.travelvcommerce.uploadservice.entity.VideoUrl;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoUpdateService {
    Video getVideo(String userId, String videoId);
    DenormalizedVideoDto updateThumbnail(String userId, String videoId,
                                         MultipartFile thumbnail, AwsS3Service awsS3Service);
    DenormalizedVideoDto updateTags(String userId, String videoId, List<String> tagIdList);
    DenormalizedVideoDto updateAds(String userId, String videoId, List<AdDto.AdModifyRequestDto> adModifyRequestDtoList);
    DenormalizedVideoDto updateVideoName(String userId, String videoId, String videoName);
    List<String> updateUploader(String userId, UploaderDto.UploaderModifyRequestDto uploaderModifyRequestDto);
}
