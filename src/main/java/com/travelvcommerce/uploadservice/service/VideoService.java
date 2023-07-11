package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.vo.RequestUpload;

public interface VideoService {
    public void saveVideo(String sellerId, RequestUpload requestUpload, String videoUrl, String thumbnailUrl);
}
