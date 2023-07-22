package com.travelvcommerce.contentslaveservice.controller;

import com.travelvcommerce.contentslaveservice.dto.ResponseDto;
import com.travelvcommerce.contentslaveservice.dto.VideoDto;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("content-slave-service")
public class VideoPageUserController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserPersonalizedService userPersonalizedService;

    // 전체 영상 조회
    @GetMapping("/videos/sort")
    public ResponseEntity<ResponseDto> getVideos(@RequestParam("q") String q,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size) {
        // q로 받은 정렬 타입 검증
        if (!UserSortTypes.contains(q.toUpperCase())) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid sort type");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        // q로 받은 정렬타입 recent -> createdAt, 도큐먼트 key 매칭
        if (q.equals("recent")) {
            q = "createdAt";
        }

        Map<String, Object> videoPagePayload;


        // 비디오 조회 로직 호출
        try {
            Page<VideoDto.VideoListResponseDto> videoPage = videoService.getVideos(q, page, size);

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

    // 사용자 개인화된 영상 조회
    @GetMapping("/videos/{userId}/personalized")
    public ResponseEntity<ResponseDto> getPersonalizedVideos(@PathVariable(name = "userId") String userId,
                                                             @RequestParam("q") String q,
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

        Map<String, Object> videoPagePayload;

        // 비디오 조회 로직 호출
        try {
            Page<VideoDto.VideoListResponseDto> videoPage = videoService.getVideosByIdList(idType, idList, page, size);

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
    // VideoPageUserController.java
    @GetMapping("/videos/search")
    public ResponseEntity<ResponseDto> searchVideos(@RequestParam("type") String type,
                                                    @RequestParam("q") String q,
                                                    @RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        try {
            Page<VideoDto.VideoListResponseDto> videoPage = videoService.searchVideos(type, q, page, size);

            Map<String, Object> videoPagePayload = new LinkedHashMap<>() {{
                put("totalPages", videoPage.getTotalPages());
                put("currentPage", videoPage.getNumber());
                put("hasNext", videoPage.hasNext());
                put("pageSize", videoPage.getSize());
                put("totalElements", videoPage.getTotalElements());
                put("videos", videoPage.getContent());
            }};

            ResponseDto responseDto = ResponseDto.buildResponseDto(videoPagePayload);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.buildResponseDto(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
    }

}
