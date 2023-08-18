package com.tavelvcommerce.commentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavelvcommerce.commentservice.dto.CommentDto;
import com.tavelvcommerce.commentservice.dto.CommentPagePayloadDto;
import com.tavelvcommerce.commentservice.dto.ResponseDto;
import com.tavelvcommerce.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment-service")
public class CommentController {
    private final ObjectMapper objectMapper;
    private final CommentService commentService;

    @PostMapping("/comments/{videoId}")
    public ResponseEntity<ResponseDto> createComment(@RequestHeader("Authorization") String id,
                                                     @PathVariable(name = "videoId") String videoId,
                                                     @RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        if (!id.equals(commentRequestDto.getUserId())) {
            ResponseDto responseDto = ResponseDto.builder().error("Invalid id").build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
        }

        String commentId = UUID.randomUUID().toString();
        String userId = commentRequestDto.getUserId();
        String nickname = commentRequestDto.getNickname();
        String content = commentRequestDto.getContent();

        CommentDto.CommentCreateResponseDto commentCreateResponseDto;
        try {
            commentCreateResponseDto = commentService.createComment(commentId, videoId, userId, content);
        }catch (NoSuchElementException e){
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.builder().payload(objectMapper.convertValue(commentCreateResponseDto, Map.class)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/comments/{videoId}/{commentId}")
    public ResponseEntity<ResponseDto> updateComment(@RequestHeader("Authorization") String id,
                                                     @PathVariable(name = "commentId") String commentId,
                                                     @RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        if (!id.equals(commentRequestDto.getUserId())) {
            ResponseDto responseDto = ResponseDto.builder().error("Invalid id").build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
        }

        String userId = commentRequestDto.getUserId();
        String content = commentRequestDto.getContent();

        CommentDto.CommentUpdateResponseDto commentUpdateResponseDto;
        try {
            commentUpdateResponseDto = commentService.updateComment(commentId, userId, content);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.builder().payload(objectMapper.convertValue(commentUpdateResponseDto, Map.class)).build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/comments/{videoId}/{commentId}/delete")
    public ResponseEntity<ResponseDto> userDeleteComment(@RequestHeader("Authorization") String id,
                                                         @PathVariable(name = "commentId") String commentId,
                                                         @RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        if (!id.equals(commentRequestDto.getUserId())) {
            ResponseDto responseDto = ResponseDto.builder().error("Invalid id").build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
        }

        String userId = commentRequestDto.getUserId();

        try {
            commentService.userDeleteComment(commentId, userId);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/comments/{sellerId}/{commentId}")
    public ResponseEntity<ResponseDto> sellerDeleteComment(@RequestHeader("Authorization") String id,
                                                           @PathVariable(name = "sellerId") String sellerId,
                                                           @PathVariable(name = "commentId") String commentId) {
        if (!id.equals(sellerId)) {
            ResponseDto responseDto = ResponseDto.builder().error("Invalid id").build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
        }

        try {
            commentService.sellerDeleteComment(commentId, sellerId);
        } catch (IllegalArgumentException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } catch (NoSuchElementException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/comments/video")
    public ResponseEntity<ResponseDto> userVideoGetComments(@RequestParam(name = "videoId") String videoId,
                                                            @RequestParam(name = "page") int page,
                                                            @RequestParam(name = "size") int size) {

        Page<CommentDto.CommentResponseDto> commentResponseDtoPage;
        try {
            commentResponseDtoPage = commentService.userVideoGetComments(videoId, page, size);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
        CommentPagePayloadDto commentPagePayloadDto = CommentPagePayloadDto.builder()
                .totalPages(commentResponseDtoPage.getTotalPages())
                .currentPage(commentResponseDtoPage.getNumber())
                .hasNext(commentResponseDtoPage.hasNext())
                .pageSize(commentResponseDtoPage.getSize())
                .totalElements(commentResponseDtoPage.getTotalElements())
                .comments(commentResponseDtoPage.getContent())
                .build();

        ResponseDto responseDto = ResponseDto.builder().payload(objectMapper.convertValue(commentPagePayloadDto, Map.class)).build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/comments/user")
    public ResponseEntity<ResponseDto> userGetComments(@RequestHeader("Authorization") String id,
                                                       @RequestParam(name = "userId") String userId,
                                                       @RequestParam(name = "page") int page,
                                                       @RequestParam(name = "size") int size) {
        if (!id.equals(userId)) {
            ResponseDto responseDto = ResponseDto.builder().error("Invalid id").build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
        }

        Page<CommentDto.CommentResponseDto> commentResponseDtoPage;
        try {
            commentResponseDtoPage = commentService.userGetComments(userId, page, size);
        } catch (RuntimeException e) {
            ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
        CommentPagePayloadDto commentPagePayloadDto = CommentPagePayloadDto.builder()
                .totalPages(commentResponseDtoPage.getTotalPages())
                .currentPage(commentResponseDtoPage.getNumber())
                .hasNext(commentResponseDtoPage.hasNext())
                .pageSize(commentResponseDtoPage.getSize())
                .totalElements(commentResponseDtoPage.getTotalElements())
                .comments(commentResponseDtoPage.getContent())
                .build();

        ResponseDto responseDto = ResponseDto.builder().payload(objectMapper.convertValue(commentPagePayloadDto, Map.class)).build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
