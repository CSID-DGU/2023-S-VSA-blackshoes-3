package com.travelvcommerce.personalizedservice.controller;

import com.travelvcommerce.personalizedservice.dto.ResponseDto;
import com.travelvcommerce.personalizedservice.dto.VideoDto;
import com.travelvcommerce.personalizedservice.service.CustomBadRequestException;
import com.travelvcommerce.personalizedservice.service.ResourceNotFoundException;
import com.travelvcommerce.personalizedservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/personalized-service")
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/{userId}/videos/history")
    public ResponseEntity<ResponseDto> viewVideo(@PathVariable String userId, @RequestBody VideoDto.ViewVideoRequestDto viewVideoRequestDto) {
        try {
            Map<String, String> viewVideoResponse = videoService.viewVideo(userId, viewVideoRequestDto);

            ResponseDto responseDto = ResponseDto.builder().payload(viewVideoResponse).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().error(e.getMessage()).build());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }

    @DeleteMapping("/{userId}/videos/history/{videoId}")
    public ResponseEntity<ResponseDto> deleteViewVideo(@PathVariable String userId, @PathVariable String videoId) {
        try {
            videoService.unviewVideo(userId, videoId);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().error(e.getMessage()).build());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }

    @GetMapping("/{userId}/videos/history")
    public ResponseEntity<ResponseDto> getViewedVideosByUserId(@PathVariable String userId, @RequestParam int page, @RequestParam int size) {
        try {
            Page<String> viewVideoResponsePage = videoService.getViewVideoIdListWithViewCount(userId, page, size);

            VideoDto.ViewVideoPagePayloadDto viewVideoPagePayloadDto = VideoDto.ViewVideoPagePayloadDto.builder()
                    .totalPages(viewVideoResponsePage.getTotalPages())
                    .totalElements(viewVideoResponsePage.getTotalElements())
                    .currentPage(viewVideoResponsePage.getNumber())
                    .hasNext(viewVideoResponsePage.hasNext())
                    .pageSize(viewVideoResponsePage.getSize())
                    .viewVideoIdList(viewVideoResponsePage.getContent())
                    .build();

            Map<String, Object> getViewedVideosIdResponse = new HashMap<>();
            getViewedVideosIdResponse.put("userId", userId);
            getViewedVideosIdResponse.put("viewedVideos", viewVideoPagePayloadDto);

            ResponseDto responseDto = ResponseDto.builder().payload(getViewedVideosIdResponse).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().error(e.getMessage()).build());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }



    // 비디오 좋아요
    @PostMapping("/{userId}/videos/liked")
    public ResponseEntity<ResponseDto> likeVideo(@PathVariable String userId, @RequestBody VideoDto.LikeVideoRequestDto likeVideoRequestDto) {
        try {
            Map<String, String> likeVideoResponse = videoService.likeVideo(userId, likeVideoRequestDto);
            ResponseDto responseDto = ResponseDto.builder().payload(likeVideoResponse).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().error(e.getMessage()).build());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }

    // 비디오 좋아요 해제
    @DeleteMapping("/{userId}/videos/liked/{videoId}")
    public ResponseEntity<ResponseDto> unlikeVideo(@PathVariable String userId, @PathVariable String videoId) {
        try {
            videoService.unlikeVideo(userId, videoId);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().error(e.getMessage()).build());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }

    // 좋아요한 비디오 리스트
    @GetMapping("/{userId}/videos/liked")
    public ResponseEntity<ResponseDto> getLikedVideosByUserId(@PathVariable String userId, @RequestParam int page, @RequestParam int size) {
        try {
            Page<String> likedVideosIdPage = videoService.getLikedVideoIdList(userId, page, size);

            VideoDto.LikeVideoPagePayloadDto likeVideoPagePayloadDto = VideoDto.LikeVideoPagePayloadDto.builder()
                    .totalPages(likedVideosIdPage.getTotalPages())
                    .totalElements(likedVideosIdPage.getTotalElements())
                    .currentPage(likedVideosIdPage.getNumber())
                    .hasNext(likedVideosIdPage.hasNext())
                    .pageSize(likedVideosIdPage.getSize())
                    .likedVideoIdList(likedVideosIdPage.getContent())
                    .build();

            Map<String, Object> getLikedVideosIdResponse = new HashMap<>();
            getLikedVideosIdResponse.put("userId", userId);
            getLikedVideosIdResponse.put("likedVideos", likeVideoPagePayloadDto);

            ResponseDto responseDto = ResponseDto.builder().payload(getLikedVideosIdResponse).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().error(e.getMessage()).build());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }



}