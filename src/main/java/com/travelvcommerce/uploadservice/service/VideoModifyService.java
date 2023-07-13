package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.entity.Video;
import org.springframework.web.multipart.MultipartFile;

public interface VideoModifyService {
    public Video getVideo(String userId, String videoId);
    public String getThumbnailS3Key(Video video);

    public void updateThumbnail(Video video);
}
