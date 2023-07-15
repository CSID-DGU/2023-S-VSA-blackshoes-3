package com.travelvcommerce.contentslaveservice.controller;

import com.travelvcommerce.contentslaveservice.dto.ResponseDto;
import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("content-slave-service")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @GetMapping("/videos/{videoId}")
    public ResponseEntity<ResponseDto> getVideo(@PathVariable(name = "videoId") String videoId) {
        try {
            VideoDto.VideoDetailResponseDto videoDetailResponseDto = videoService.getVideo(videoId);
            ResponseDto responseDto = ResponseDto.buildResponseDto(Collections.singletonMap("video", videoDetailResponseDto));
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
