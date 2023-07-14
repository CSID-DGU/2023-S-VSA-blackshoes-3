package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.TagDto;
import com.travelvcommerce.uploadservice.entity.Video;

import java.util.List;

public interface VideoModifyService {
    public Video getVideo(String userId, String videoId);
    public String getThumbnailS3Key(Video video);
    public void updateThumbnail(Video video);
    public void updateTag(String userId, String videoId, List<String> tagIdList);
}
