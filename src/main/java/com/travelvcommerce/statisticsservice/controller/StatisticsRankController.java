package com.travelvcommerce.statisticsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.RankDto;
import com.travelvcommerce.statisticsservice.dto.RankResponseDto;
import com.travelvcommerce.statisticsservice.dto.ResponseDto;
import com.travelvcommerce.statisticsservice.service.StatisticsRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics-service")
public class StatisticsRankController {
    @Autowired
    private StatisticsRankService statisticsRankService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/rank/videos/likes/{sellerId}")
    public ResponseEntity<ResponseDto> getLikeRank(@PathVariable(name = "sellerId") String sellerId) {
        List<RankDto.VideoLikeRankDto> ranks;

        try {
            ranks = statisticsRankService.getVideoLikeTop10(sellerId);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        RankResponseDto rankResponseDto = RankResponseDto.builder()
                .videoLikeRank(ranks)
                .build();

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(rankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rank/videos/views/{sellerId}")
    public ResponseEntity<ResponseDto> getViewRank(@PathVariable(name = "sellerId") String sellerId) {
        List<RankDto.VideoViewRankDto> ranks;

        try {
            ranks = statisticsRankService.getVideoViewTop10(sellerId);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        RankResponseDto rankResponseDto = RankResponseDto.builder()
                .videoViewRank(ranks)
                .build();

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(rankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rank/tags/views/{sellerId}")
    public ResponseEntity<ResponseDto> getTagViewRank(@PathVariable(name = "sellerId") String sellerId) {
        List<RankDto.TagViewRankDto> ranks;

        try {
            ranks = statisticsRankService.getTagViewTop10(sellerId);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        RankResponseDto rankResponseDto = RankResponseDto.builder()
                .tagViewRank(ranks)
                .build();

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(rankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rank/ads/clicks/{sellerId}")
    public ResponseEntity<ResponseDto> getAdClickRank(@PathVariable(name = "sellerId") String sellerId) {
        List<RankDto.VideoAdClickRankDto> ranks;

        try {
            ranks = statisticsRankService.getAdClickTop10(sellerId);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        RankResponseDto rankResponseDto = RankResponseDto.builder()
                .videoAdClickRank(ranks)
                .build();

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(rankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
