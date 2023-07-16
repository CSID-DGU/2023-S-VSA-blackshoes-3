package com.travelvcommerce.contentslaveservice.controller;

import com.travelvcommerce.contentslaveservice.dto.ResponseDto;
import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.service.VideoService;
import com.travelvcommerce.contentslaveservice.vo.VideoIdType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("content-slave-service")
public class VideoDetailController {
    @Autowired
    private VideoService videoService;

    // 개별 영상 조회
    @GetMapping("/videos/video")
    public ResponseEntity<ResponseDto> getVideo(@RequestParam(name = "type") String idType,
                                                @RequestParam(name = "q") String id) {


        // type으로 받은 idType 검증
        if (!VideoIdType.contains(idType.toUpperCase())) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid id type");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        // idType이 adId일 경우 videoAds.adId로 변경
        if (idType.toUpperCase().equals(VideoIdType.ADID.toString())) {
            idType = "videoAds.adId";
        }

        VideoDto.VideoDetailResponseDto videoDetailResponseDto;

        try {
            videoDetailResponseDto = videoService.getVideoById(idType, id);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(Collections.singletonMap("video", videoDetailResponseDto));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
