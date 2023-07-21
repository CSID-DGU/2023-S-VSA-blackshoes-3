package com.travelvcommerce.contentslaveservice.service;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

public interface DatabaseService {
    void create(VideoDto videoDto);
    void update(String videoId, VideoDto videoDto);
    void delete(String videoId, VideoDto videoDto);
}
