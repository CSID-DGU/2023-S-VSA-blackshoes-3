package com.travelvcommerce.statisticsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.statisticsservice.dto.*;
import com.travelvcommerce.statisticsservice.service.KafkaVideoInfoProducerService;
import com.travelvcommerce.statisticsservice.exception.UserAlreadyLikedVideoException;
import com.travelvcommerce.statisticsservice.exception.UserAlreadyViewedVideoException;
import com.travelvcommerce.statisticsservice.service.StatisticsUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/statistics-service")
public class StatisticsUpdateController {
    @Autowired
    private StatisticsUpdateService statisticsUpdateService;
    @Autowired
    private KafkaVideoInfoProducerService kafkaVideoInfoProducerService;
    @Autowired
    private ObjectMapper objectMapper;

    @PutMapping("/{videoId}/views")
    public ResponseEntity<ResponseDto> increaseViewCount(@PathVariable("videoId") String videoId,
                                                         @RequestBody ViewCountDto.ViewCountRequestDto viewCountRequestDto) {
        String userId = viewCountRequestDto.getUserId();
        VideoCountInfoDto videoCountInfoDto;

        try {
            videoCountInfoDto = statisticsUpdateService.increaseVideoViewCount(videoId, userId);
        } catch (UserAlreadyViewedVideoException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        try {
            List<String> tagIdList = viewCountRequestDto.getTagIdList();
            for (String tagId : tagIdList) {
                statisticsUpdateService.increaseTagViewCount(videoId, tagId, userId);
            }
        } catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        kafkaVideoInfoProducerService.updateVideoStatistics(videoCountInfoDto);

        ViewCountDto.ViewCountResponseDto viewCountResponseDto = ViewCountDto.ViewCountResponseDto.builder()
                .videoId(videoId)
                .updatedAt(LocalDateTime.now().toString())
                .build();

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(viewCountResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/{videoId}/likes")
    public ResponseEntity<ResponseDto> increaseLikeCount(@PathVariable("videoId") String videoId,
                                                         @RequestBody LikeDto.LikeRequestDto likeRequestDto) {
        String userId = likeRequestDto.getUserId();
        String action = likeRequestDto.getAction();
        VideoCountInfoDto videoCountInfoDto;
        try {
            if (action.equals("like")) {
                videoCountInfoDto = statisticsUpdateService.increaseVideoLikeCount(videoId, userId);
            } else if (action.equals("dislike")) {
                videoCountInfoDto = statisticsUpdateService.decreaseVideoLikeCount(videoId, userId);
            } else {
                throw new IllegalArgumentException("action must be like or dislike");
            }
        } catch (UserAlreadyLikedVideoException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        kafkaVideoInfoProducerService.updateVideoStatistics(videoCountInfoDto);

        LikeDto.LikeResponseDto likeResponseDto = LikeDto.LikeResponseDto.builder()
                .videoId(videoId)
                .updatedAt(LocalDateTime.now().toString())
                .build();
        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(likeResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/{adId}/adClicks")
    public ResponseEntity<ResponseDto> increaseAdClickCount(@PathVariable("adId") String adId,
                                                            @RequestBody AdClickDto.AdClickRequestDto adClickRequestDto) {
        String userId = adClickRequestDto.getUserId();
        VideoCountInfoDto videoCountInfoDto;

        try {
            videoCountInfoDto = statisticsUpdateService.increaseVideoAdClickCount(adId, userId);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        kafkaVideoInfoProducerService.updateVideoStatistics(videoCountInfoDto);

        AdClickDto.AdClickResponseDto adClickResponseDto = AdClickDto.AdClickResponseDto.builder()
                .adId(adId)
                .updatedAt(LocalDateTime.now().toString())
                .build();
        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(adClickResponseDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
