package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.entity.Video;
import org.springframework.data.domain.Page;

public interface VideoService {
    Page<Video> getVideos(String q, int page, int size);
}
