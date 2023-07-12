package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.VideoDto;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    public String uploadVideo(String fileName, MultipartFile videoFile);
    public String encodeVideo(String filePath);
    public void saveVideo(String sellerId, VideoDto.VideoUploadRequestDto videoUploadRequestDto, String videoUrl, String thumbnailUrl);
}
