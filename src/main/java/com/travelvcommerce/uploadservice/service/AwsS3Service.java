package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.vo.S3Thumbnail;
import com.travelvcommerce.uploadservice.vo.S3Video;
import org.springframework.web.multipart.MultipartFile;


public interface AwsS3Service {
    public S3Video uploadEncodedVideo(String userId, String videoId, String filePath);
    public S3Thumbnail uploadThumbnail(String userId, String videoId,  MultipartFile multipartFile);
    public S3Thumbnail updateThumbnail(String s3Key, MultipartFile multipartFile);
    public void deleteVideo(String s3Key);
}
