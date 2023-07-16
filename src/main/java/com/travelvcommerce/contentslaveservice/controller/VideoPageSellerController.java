package com.travelvcommerce.contentslaveservice.controller;

import com.travelvcommerce.contentslaveservice.dto.ResponseDto;
import com.travelvcommerce.contentslaveservice.dto.VideoDto;
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

    // 판매자별 전체 영상 조회
    @GetMapping("/videos/{sellerId}/sort")
    public ResponseEntity<ResponseDto> getVideosBySellerId(@PathVariable(name = "sellerId") String sellerId,
                                                           @RequestParam("q") String q,
                                                           @RequestParam("page") int page,
                                                           @RequestParam("size") int size) {
        // q로 받은 정렬 타입 검증
        if (!SellerSortTypes.contains(q.toUpperCase())) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid sort type");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        // q로 받은 정렬타입 recent -> createdAt, 도큐먼트 key 매칭
        if (q.equals("recent")) {
            q = "createdAt";
        }

        Map<String, Object> videoPagePayload;

        try {
            Page<VideoDto.VideoListResponseDto> videoPage = videoService.getVideosBySellerId(sellerId, q, page, size);

            videoPagePayload = new LinkedHashMap<>() {{
                put("totalPages", videoPage.getTotalPages());
                put("currentPage", videoPage.getNumber());
                put("hasNext", videoPage.hasNext());
                put("pageSize", videoPage.getSize());
                put("totalElements", videoPage.getTotalElements());
                put("videos", videoPage.getContent());
            }};

        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(videoPagePayload);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
