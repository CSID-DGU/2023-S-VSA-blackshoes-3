package com.travelvcommerce.statisticsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.RankResponseDto;
import com.travelvcommerce.statisticsservice.dto.ResponseDto;
import com.travelvcommerce.statisticsservice.service.StatisticsRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/statistics-service")
public class StatisticsRankController {
    @Autowired
    private StatisticsRankService statisticsRankService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/rank/videos/views/{sellerId}")
    public ResponseEntity<ResponseDto> getViewRank(@PathVariable(name = "sellerId") String sellerId) {
        RankResponseDto.VideoViewRankResponseDto videoViewRankResponseDto;

        try {
            videoViewRankResponseDto = statisticsRankService.getVideoViewTop10(sellerId);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoViewRankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rank/tags/views/{sellerId}")
    public ResponseEntity<ResponseDto> getTagViewRank(@PathVariable(name = "sellerId") String sellerId) {
        RankResponseDto.TagViewRankResponseDto tagRankResponseDto;

        try {
            tagRankResponseDto = statisticsRankService.getTagViewTop10(sellerId);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(tagRankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rank/videos/likes/{sellerId}")
    public ResponseEntity<ResponseDto> getLikeRank(@PathVariable(name = "sellerId") String sellerId) {
        RankResponseDto.VideoLikeRankResponseDto videoLikeRankResponseDto;

        try {
            videoLikeRankResponseDto = statisticsRankService.getVideoLikeTop10(sellerId);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoLikeRankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rank/videos/adClicks/{sellerId}")
    public ResponseEntity<ResponseDto> getAdClickRank(@PathVariable(name = "sellerId") String sellerId) {
        RankResponseDto.VideoAdClickRankResponseDto videoAdClickRankResponseDto;

        try {
            videoAdClickRankResponseDto = statisticsRankService.getAdClickTop10(sellerId);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoAdClickRankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
