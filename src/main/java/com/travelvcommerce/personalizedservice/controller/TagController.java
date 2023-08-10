package com.travelvcommerce.personalizedservice.controller;

import com.travelvcommerce.personalizedservice.dto.ResponseDto;
import com.travelvcommerce.personalizedservice.dto.TagDto;
import com.travelvcommerce.personalizedservice.entity.ViewTag;
import com.travelvcommerce.personalizedservice.repository.SubscribedTagRepository;
import com.travelvcommerce.personalizedservice.repository.ViewTagRepository;
import com.travelvcommerce.personalizedservice.service.CustomBadRequestException;
import com.travelvcommerce.personalizedservice.service.TagService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("personalized-service/tags")
public class TagController {

    private final TagService tagService;


    //TagId가 유효한지 아닌지 예외처리 추가해야함
    @PostMapping("/{userId}/init")
    public ResponseEntity<ResponseDto> InitSubscribedTagList(@PathVariable String userId, @RequestBody TagDto.InitTagListRequestDto initTagListRequestDto){
        Map<String, String> initTagListResponse = tagService.initSubscribedTagList(userId, initTagListRequestDto);

        ResponseDto responseDto = ResponseDto.builder()
                .payload(initTagListResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{userId}/subscribed")
    public ResponseEntity<ResponseDto> getSubscribedTagList(@PathVariable String userId){

        Map<String, Object> subscribedTagListResponse = new HashMap<>();
        subscribedTagListResponse.put("userId", userId);
        subscribedTagListResponse.put("subscribedTagList", tagService.getSubscribedTagList(userId));

        ResponseDto responseDto = ResponseDto.builder().
                payload(subscribedTagListResponse).
                build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{userId}/viewed")
    public ResponseEntity<ResponseDto> getViewedTagList(@PathVariable String userId) {
        try {
            List<Map<String, Object>> viewedTagListWithDetails = tagService.getViewedTagList(userId);

            Map<String, Object> viewedTagListResponse = new HashMap<>();
            viewedTagListResponse.put("userId", userId);
            viewedTagListResponse.put("viewedTags", viewedTagListWithDetails);

            ResponseDto responseDto = ResponseDto.builder().payload(viewedTagListResponse).build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (CustomBadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }


    //해당하는 유저 없는 경우 서비스에서 예외처리 되어있음.
    @PostMapping("/{userId}/subscribe")
    public ResponseEntity<ResponseDto> subscribeTag(@PathVariable String userId, @RequestBody TagDto.SubscribeTagRequestDto subscribeTagRequestDto){
        try {
            Map<String, String> subscribeTagResponse = tagService.subscribeTag(userId, subscribeTagRequestDto);

            ResponseDto responseDto = ResponseDto.builder()
                    .payload(subscribeTagResponse)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch(CustomBadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }
    @DeleteMapping("/{userId}/subscribe")
    public ResponseEntity<ResponseDto> unsubscribeTag(@PathVariable String userId, @RequestBody TagDto.UnsubscribeTagRequestDto unsubscribeTagRequestDto){
        try{
        Map<String, String> unsubscribeTagResponse = tagService.unsubscribeTag(userId, unsubscribeTagRequestDto);

        ResponseDto responseDto = ResponseDto.builder()
                .payload(unsubscribeTagResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }catch(CustomBadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().error(e.getMessage()).build());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().error("서버 내부 오류").build());
        }
    }

    //조회한 태그 추가, 삭제는 없음
    @PostMapping("/{userId}/view")
    public ResponseEntity<ResponseDto> viewTag(@PathVariable String userId, @RequestBody TagDto.ViewTagRequestDto viewTagRequestDto){
        Map<String, String> viewTagResponse = tagService.viewTag(userId, viewTagRequestDto);

        ResponseDto responseDto = ResponseDto.builder()
                .payload(viewTagResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
