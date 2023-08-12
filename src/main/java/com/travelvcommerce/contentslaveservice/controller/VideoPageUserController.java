package com.travelvcommerce.contentslaveservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelvcommerce.contentslaveservice.dto.ResponseDto;
import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.dto.VideoPagePayloadDto;
import com.travelvcommerce.contentslaveservice.service.UserPersonalizedService;
import com.travelvcommerce.contentslaveservice.service.VideoService;
import com.travelvcommerce.contentslaveservice.vo.UserPersonalizedData;
import com.travelvcommerce.contentslaveservice.vo.UserPersonalizedTypes;
import com.travelvcommerce.contentslaveservice.vo.UserSortTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("content-slave-service")
public class VideoPageUserController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserPersonalizedService userPersonalizedService;
    @Autowired
    private ObjectMapper objectMapper;

    // 전체 영상 조회
    @GetMapping("/videos/sort")
    public ResponseEntity<ResponseDto> getVideos(@RequestParam("s") String s,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size) {
        // q로 받은 정렬 타입 검증하고 sortType 변수에 저장
        String sortType;
        try {
            sortType = validateAndReturnSortType(s);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        VideoPagePayloadDto videoPagePayloadDto;


        // 비디오 조회 로직 호출
        try {
            Page<VideoDto.VideoListResponseDto> videoPage = videoService.getVideos(sortType, page, size);

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

    // tagId로 영상 조회
    @GetMapping("/videos")
    public ResponseEntity<ResponseDto> getVideosByTag(@RequestParam("tagId") String tagId,
                                                      @RequestParam("s") String s,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int size) {

        // s로 받은 정렬 타입 검증하고 sortType 변수에 저장
        String sortType;
        try {
            sortType = validateAndReturnSortType(s);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        VideoPagePayloadDto videoPagePayloadDto;

        // 비디오 조회 로직 호출
        try {
            Page<VideoDto.VideoListResponseDto> videoPage = videoService.getVideosByTagId(tagId, sortType, page, size);

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

    // 사용자 개인화된 영상 조회
    @GetMapping("/videos/{userId}/personalized")
    public ResponseEntity<ResponseDto> getPersonalizedVideos(@PathVariable(name = "userId") String userId,
                                                             @RequestParam("q") String q,
                                                             @RequestParam("s") String s,
                                                             @RequestParam("page") int page,
                                                             @RequestParam("size") int size) {
        String personalizedType = q.toUpperCase();

        // q로 받은 personalizedType 해당 서비스 호출, 개인화 정보 조회
        UserPersonalizedData userPersonalizedData;
        if (personalizedType.equals(UserPersonalizedTypes.HISTORY.toString())) {
            userPersonalizedData = userPersonalizedService.getPersonalizedHistory(userId);
        } else if (personalizedType.equals(UserPersonalizedTypes.TAG.toString())) {
            userPersonalizedData = userPersonalizedService.getPersonalizedTags(userId);
        } else if (personalizedType.equals(UserPersonalizedTypes.LIKE.toString())) {
            userPersonalizedData = userPersonalizedService.getPersonalizedLikes(userId);
        } else {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid personalized type");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        // 개인화 정보의 idType(조회에 사용할 key), idList(조회에 사용할 value list) 변수 저장
        String idType = userPersonalizedData.getIdType();
        List<String> idList = userPersonalizedData.getIdList();

        // s로 받은 정렬 타입 검증하고 sortType 변수에 저장
        String sortType;
        try {
            sortType = validateAndReturnSortType(s);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        VideoPagePayloadDto videoPagePayloadDto;

        // 비디오 조회 로직 호출
        try {
            Page<VideoDto.VideoListResponseDto> videoPage
                    = videoService.getVideosByIdList(idType, idList, sortType, page, size);

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

    // VideoPageUserController.java
    @GetMapping("/videos/search")
    public ResponseEntity<ResponseDto> searchVideos(@RequestParam("type") String type,
                                                    @RequestParam("q") String q,
                                                    @RequestParam("s") String s,
                                                    @RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        // s로 받은 정렬 타입 검증하고 sortType 변수에 저장
        String sortType;
        try {
            sortType = validateAndReturnSortType(s);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        VideoPagePayloadDto videoPagePayloadDto;
        try {
            Page<VideoDto.VideoListResponseDto> videoPage = videoService.searchVideos(type, q, sortType, page, size);

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

    @GetMapping("/videos")
    public ResponseEntity<ResponseDto> getVideosByIdList(@RequestParam(name = "ids") List<String> videoIdList) {
        VideoPagePayloadDto videoPagePayloadDto;

        try {
            List<VideoDto.VideoListResponseDto> videoList = videoService.getVideosByVideoIdList(videoIdList);

            videoPagePayloadDto = VideoPagePayloadDto.builder()
                    .videos(videoList)
                    .build();

        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.buildResponseDto(objectMapper.convertValue(videoPagePayloadDto, Map.class));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    private static String validateAndReturnSortType(String s) {
        // s로 받은 정렬 타입 검증
        if (!UserSortTypes.contains(s.toUpperCase())) {
            throw new IllegalArgumentException("Invalid sort type");
        }
        // s로 받은 정렬타입 recent -> createdAt, 도큐먼트 key 매칭
        if (s.equals("recent")) {
            s = "createdAt";
        }

        return s;
    }
}
