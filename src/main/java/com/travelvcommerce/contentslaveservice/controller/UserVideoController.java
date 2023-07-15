package com.travelvcommerce.contentslaveservice.controller;

import com.travelvcommerce.contentslaveservice.dto.ResponseDto;
import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.util.Map;

@RestController
@RequestMapping("content-slave-service")
public class UserVideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/videos/sort")
    public ResponseEntity<ResponseDto> getVideos(@QueryParam("q") String q, @QueryParam("page") int page, @QueryParam("size") int size) {
        if (q.equals("recent")) {
            q = "createdAt";
        }
        try {
        Page<VideoDto.VideoListResponseDto> videoPage = videoService.getVideos(q, page, size);
        Map<String, Object> payload = Map.of("videos", videoPage.getContent(),
                "totalPages", videoPage.getTotalPages(),
                "currentPage", videoPage.getNumber(),
                "hasNext", videoPage.hasNext(),
                "pageSize", videoPage.getSize(),
                "totalElements", videoPage.getTotalElements());
        ResponseDto responseDto = ResponseDto.buildResponseDto(payload);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
