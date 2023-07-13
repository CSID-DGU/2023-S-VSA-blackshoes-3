package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.vo.S3Video;
import org.springframework.web.multipart.MultipartFile;


public interface AwsS3Service {
    public S3Video uploadEncodedVideo(String fileName, String filePath);
    public String uploadThumbnail(String fileName, MultipartFile multipartFile);
}
