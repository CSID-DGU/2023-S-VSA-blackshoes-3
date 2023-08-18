package com.travelvcommerce.statisticsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.RankResponseDto;
import com.travelvcommerce.statisticsservice.dto.ResponseDto;
import com.travelvcommerce.statisticsservice.service.StatisticsRankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/statistics-service")
public class StatisticsRankController {
    @Autowired
    private StatisticsRankService statisticsRankService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/rank/videos/views/{sellerId}")
    public ResponseEntity<ResponseDto> getViewRank(@RequestHeader("Authorization") String id,
                                                   @PathVariable(name = "sellerId") String sellerId,
                                                   @RequestParam(name = "size", defaultValue = "5") int size,
                                                   @RequestParam(name = "refresh", defaultValue = "false") boolean refresh) {
        if (!id.equals(sellerId)) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid id");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }

        RankResponseDto.VideoViewRankResponseDto videoViewRankResponseDto;

        try {
            videoViewRankResponseDto = statisticsRankService.getVideoViewRank(sellerId, size, refresh);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoViewRankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rank/tags/views/{sellerId}")
    public ResponseEntity<ResponseDto> getTagViewRank(@RequestHeader("Authorization") String id,
                                                      @PathVariable(name = "sellerId") String sellerId,
                                                      @RequestParam(name = "size", defaultValue = "5") int size,
                                                      @RequestParam(name = "refresh", defaultValue = "false") boolean refresh) {
        if (!id.equals(sellerId)) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid id");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }

        RankResponseDto.TagViewRankResponseDto tagRankResponseDto;

        try {
            tagRankResponseDto = statisticsRankService.getTagViewRank(sellerId, size, refresh);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(tagRankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rank/videos/likes/{sellerId}")
    public ResponseEntity<ResponseDto> getLikeRank(@RequestHeader("Authorization") String id,
                                                   @PathVariable(name = "sellerId") String sellerId,
                                                   @RequestParam(name = "size", defaultValue = "5") int size,
                                                   @RequestParam(name = "refresh", defaultValue = "false") boolean refresh) {
        if (!id.equals(sellerId)) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid id");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }

        RankResponseDto.VideoLikeRankResponseDto videoLikeRankResponseDto;

        try {
            videoLikeRankResponseDto = statisticsRankService.getVideoLikeRank(sellerId, size, refresh);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoLikeRankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rank/videos/adClicks/{sellerId}")
    public ResponseEntity<ResponseDto> getAdClickRank(@RequestHeader("Authorization") String id,
                                                      @PathVariable(name = "sellerId") String sellerId,
                                                      @RequestParam(name = "size", defaultValue = "5") int size,
                                                      @RequestParam(name = "refresh", defaultValue = "false") boolean refresh) {
        if (!id.equals(sellerId)) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid id");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }

        RankResponseDto.VideoAdClickRankResponseDto videoAdClickRankResponseDto;

        try {
            videoAdClickRankResponseDto = statisticsRankService.getAdClickRank(sellerId, size, refresh);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoAdClickRankResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
