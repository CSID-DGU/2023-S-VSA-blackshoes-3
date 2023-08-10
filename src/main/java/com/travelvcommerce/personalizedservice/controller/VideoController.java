package com.travelvcommerce.personalizedservice.controller;

import com.travelvcommerce.personalizedservice.dto.ResponseDto;
import com.travelvcommerce.personalizedservice.dto.VideoDto;
import com.travelvcommerce.personalizedservice.service.CustomBadRequestException;
import com.travelvcommerce.personalizedservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/personalized-service/videos")
public class VideoController {

    private final VideoService videoService;

    // 비디오 조회 컬럼 추가, 이미 있으면 count += 1
    @PostMapping("/history/{userId}")
    public ResponseEntity<ResponseDto> viewVideo(@PathVariable String userId, @RequestBody VideoDto.ViewVideoRequestDto viewVideoRequestDto) {
        try {
            Map<String, String> viewVideoResponse = videoService.viewVideo(userId, viewVideoRequestDto);

            ResponseDto responseDto = ResponseDto.builder().payload(viewVideoResponse).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }

    @DeleteMapping("/history/{userId}")
    public ResponseEntity<ResponseDto> unviewVideo(@PathVariable String userId, @RequestBody VideoDto.UnviewVideoRequestDto unviewVideoRequestDto) {
        try {
            Map<String, String> unviewVideoResponse = videoService.unviewVideo(userId, unviewVideoRequestDto);

            ResponseDto responseDto = ResponseDto.builder().payload(unviewVideoResponse).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);

        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<ResponseDto> getViewedVideosByUserId(@PathVariable String userId) {
        try {
            List<String> viewVideoIdList = videoService.getViewVideoIdList(userId);

            Map<String, Object> getViewedVideosIdResponse = new HashMap<>();
            getViewedVideosIdResponse.put("userId", userId);
            getViewedVideosIdResponse.put("viewedVideos", viewVideoIdList);

            ResponseDto responseDto = ResponseDto.builder().payload(getViewedVideosIdResponse).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }

    // 비디오 좋아요
    @PostMapping("/like/{userId}")
    public ResponseEntity<ResponseDto> likeVideo(@PathVariable String userId, @RequestBody VideoDto.LikeVideoRequestDto likeVideoRequestDto) {
        try {
            Map<String, String> likeVideoResponse = videoService.likeVideo(userId, likeVideoRequestDto);
            ResponseDto responseDto = ResponseDto.builder().payload(likeVideoResponse).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }

    // 비디오 좋아요 해제
    @DeleteMapping("/like/{userId}")
    public ResponseEntity<ResponseDto> unlikeVideo(@PathVariable String userId, @RequestBody VideoDto.UnlikeVideoRequestDto unlikeVideoRequestDto) {
        try {
            videoService.unlikeVideo(userId, unlikeVideoRequestDto);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }

    // 좋아요한 비디오 리스트
    @GetMapping("/like/{userId}")
    public ResponseEntity<ResponseDto> getLikedVideosByUserId(@PathVariable String userId) {
        try {
            List<String> likedVideosId = videoService.getLikedVideoIdList(userId);

            Map<String, Object> getLikedVideosIdResponse = new HashMap<>();
            getLikedVideosIdResponse.put("userId", userId);
            getLikedVideosIdResponse.put("likedVideos", likedVideosId);

            ResponseDto responseDto = ResponseDto.builder().payload(getLikedVideosIdResponse).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }


}