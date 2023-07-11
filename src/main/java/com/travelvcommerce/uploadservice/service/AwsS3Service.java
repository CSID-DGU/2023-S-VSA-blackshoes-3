package com.travelvcommerce.uploadservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
    public String uploadFile(String fileType, String fileName, MultipartFile multipartFile);
}
