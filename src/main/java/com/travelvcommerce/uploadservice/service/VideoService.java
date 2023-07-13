package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.VideoDto;
import com.travelvcommerce.uploadservice.vo.S3Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface VideoService {
    public String uploadVideo(String fileName, MultipartFile videoFile);
    public String encodeVideo(String filePath);
    public void saveVideo(String sellerId, VideoDto.VideoUploadRequestDto videoUploadRequestDto, S3Video videoUrls, String thumbnailUrls);
}
