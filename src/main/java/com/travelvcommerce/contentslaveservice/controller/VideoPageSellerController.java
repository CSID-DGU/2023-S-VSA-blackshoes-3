package com.travelvcommerce.contentslaveservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.contentslaveservice.dto.ResponseDto;
import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.dto.VideoPagePayloadDto;
import com.travelvcommerce.contentslaveservice.service.VideoService;
import com.travelvcommerce.contentslaveservice.vo.SellerSortTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/content-slave-service")
public class VideoPageSellerController {
    @Autowired
    VideoService videoService;
    @Autowired
    ObjectMapper objectMapper;

    // 판매자별 전체 영상 조회
    @GetMapping("/videos/{sellerId}/sort")
    public ResponseEntity<ResponseDto> getVideosBySellerId(@PathVariable(name = "sellerId") String sellerId,
                                                           @RequestParam("s") String s,
                                                           @RequestParam("page") int page,
                                                           @RequestParam("size") int size) {
        // q로 받은 정렬 타입 검증
        if (!SellerSortTypes.contains(s.toUpperCase())) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid sort type");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        // q로 받은 정렬타입 recent -> createdAt, 도큐먼트 key 매칭
        if (s.equals("recent")) {
            s = "createdAt";
        }

        VideoPagePayloadDto videoPagePayloadDto;

        try {
            Page<VideoDto.VideoListResponseDto> videoPage = videoService.getVideosBySellerId(sellerId, s, page, size);

            videoPagePayloadDto = VideoPagePayloadDto.builder()
                    .totalPages(videoPage.getTotalPages())
                    .currentPage(videoPage.getNumber())
                    .hasNext(videoPage.hasNext())
                    .pageSize(videoPage.getSize())
                    .totalElements(videoPage.getTotalElements())
                    .videos(videoPage.getContent())
                    .build();

        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoPagePayloadDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
