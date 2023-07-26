package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.VideoDto;
import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
import com.travelvcommerce.uploadservice.vo.S3Video;
import org.springframework.web.multipart.MultipartFile;

public interface VideoCreateService {
    public String uploadVideo(String userId, String videoId, MultipartFile videoFile);
    public String encodeVideo(String userId, String videoId, String filePath);
    public VideoDto.VideoCreateResponseDto createVideo(String sellerId, String videoId,
                          VideoDto.VideoUploadRequestDto videoUploadRequestDto,
                          S3Video videoUrls, S3Thumbnail thumbnailUrls);
}
