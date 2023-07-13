package com.travelvcommerce.uploadservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
    public String uploadEncodedVideo(String fileName, String filePath);
    public String uploadThumbnail(String fileName, MultipartFile multipartFile);
}
