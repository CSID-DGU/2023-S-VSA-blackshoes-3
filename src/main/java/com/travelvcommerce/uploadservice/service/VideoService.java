package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.VideoDto;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    public void saveVideo(String sellerId, VideoDto.VideoUploadRequestDto videoUploadRequestDto, String videoUrl, String thumbnailUrl);
}
