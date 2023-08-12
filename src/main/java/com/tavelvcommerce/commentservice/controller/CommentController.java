package com.tavelvcommerce.commentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavelvcommerce.commentservice.dto.CommentDto;
import com.tavelvcommerce.commentservice.dto.ResponseDto;
import com.tavelvcommerce.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/comments/{sellerId}/{videoId}")
    public ResponseEntity<ResponseDto> createComment(@PathVariable(name = "sellerId") String sellerId,
                                                     @PathVariable(name = "videoId") String videoId,
                                                     @RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        String commentId = UUID.randomUUID().toString();
        String userId = commentRequestDto.getUserId();
        String content = commentRequestDto.getContent();

        CommentDto.CommentCreateResponseDto commentCreateResponseDto;
        try {
            commentCreateResponseDto = commentService.createComment(commentId, sellerId, videoId, userId, content);
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

    @PutMapping("/comments/{sellerId}/{videoId}/{commentId}")
    public ResponseEntity<ResponseDto> updateComment(@PathVariable(name = "videoId") String videoId,
                                                     @PathVariable(name = "commentId") String commentId,
                                                     @RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        String userId = commentRequestDto.getUserId();
        String content = commentRequestDto.getContent();

        CommentDto.CommentUpdateResponseDto commentUpdateResponseDto;
        try {
            commentUpdateResponseDto = commentService.updateComment(commentId, videoId, userId, content);
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

    @PostMapping("/comments/{sellerId}/{videoId}/{commentId}/delete")
    public ResponseEntity<ResponseDto> deleteComment(@PathVariable(name = "videoId") String videoId,
                                                     @PathVariable(name = "commentId") String commentId,
                                                     @RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        String userId = commentRequestDto.getUserId();

        try {
            commentService.userDeleteComment(commentId, videoId, userId);
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

    @DeleteMapping("/comments/{sellerId}/{videoId}/{commentId}")
    public ResponseEntity<ResponseDto> deleteComment(@PathVariable(name = "sellerId") String sellerId,
                                                     @PathVariable(name = "videoId") String videoId,
                                                     @PathVariable(name = "commentId") String commentId) {
        try {
            commentService.sellerDeleteComment(commentId, videoId, sellerId);
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
}
