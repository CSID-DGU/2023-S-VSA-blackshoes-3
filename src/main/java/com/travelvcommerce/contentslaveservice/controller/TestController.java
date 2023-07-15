package com.travelvcommerce.contentslaveservice.controller;

import com.travelvcommerce.contentslaveservice.entity.Video;
import com.travelvcommerce.contentslaveservice.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content-slave-service")
public class TestController {
    @Autowired
    VideoRepository videoRepository;

    @GetMapping("/test")
    public ResponseEntity<List> test() {
        List<Video> videos = videoRepository.findAll();
        return ResponseEntity.ok(videos);
    }
}
