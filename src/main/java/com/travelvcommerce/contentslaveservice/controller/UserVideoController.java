package com.travelvcommerce.contentslaveservice.controller;

import com.travelvcommerce.contentslaveservice.dto.ResponseDto;
import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.service.VideoService;
import com.travelvcommerce.contentslaveservice.vo.UserSortTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("content-slave-service")
public class UserVideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/videos/sort")
    public ResponseEntity<ResponseDto> getVideos(@QueryParam("q") String q, @QueryParam("page") int page, @QueryParam("size") int size) {
        if (!UserSortTypes.contains(q.toUpperCase())) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid sort type");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        };

        if (q.equals("recent")) {
            q = "createdAt";
        }

        try {
            Page<VideoDto.VideoListResponseDto> videoPage = videoService.getVideos(q, page, size);

            Map<String, Object> payload = new LinkedHashMap<>() {{
                put("totalPages", videoPage.getTotalPages());
                put("currentPage", videoPage.getNumber());
                put("hasNext", videoPage.hasNext());
                put("pageSize", videoPage.getSize());
                put("totalElements", videoPage.getTotalElements());
                put("videos", videoPage.getContent());
            }};

            ResponseDto responseDto = ResponseDto.buildResponseDto(payload);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }
}
