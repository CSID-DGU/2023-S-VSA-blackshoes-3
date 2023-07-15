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

import javax.ws.rs.QueryParam;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("content-slave-service")
public class UserVideoController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserPersonalizedService userPersonalizedService;

    @GetMapping("/videos/sort")
    public ResponseEntity<ResponseDto> getVideos(@QueryParam("q") String q,
                                                 @QueryParam("page") int page,
                                                 @QueryParam("size") int size) {
        if (!UserSortTypes.contains(q.toUpperCase())) {
            ResponseDto responseDto = ResponseDto.buildResponseDto("Invalid sort type");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }

        if (q.equals("recent")) {
            q = "createdAt";
        }

        Map<String, Object> videoPagePayload;

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

    @GetMapping("/videos/{userId}/personalized")
    public ResponseEntity<ResponseDto> getPersonalizedVideos(@PathVariable(name = "userId") String userId,
                                                             @QueryParam("q") String q,
                                                             @QueryParam("page") int page,
                                                             @QueryParam("size") int size) {
        String personalizedType = q.toUpperCase();

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

        String idType = userPersonalizedData.getIdType();
        List<String> idList = userPersonalizedData.getIdList();
        Map<String, Object> videoPagePayload;

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
}
