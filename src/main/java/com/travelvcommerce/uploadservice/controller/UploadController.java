package com.travelvcommerce.uploadservice.controller;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
public class UploadController {
    @GetMapping("/tags")
    public ResponseEntity<ResponseTagList> getTags() {
        return ResponseEntity.status(HttpStatus.OK).body(responseTagList);
    }

    @PostMapping("/videos/{userId}")
    public ResponseEntity<ResponseVideo> uploadVideo(@PathVariable String userId, @RequestPart(value = "file") MultipartFile multipartFile) {
        return ResponseEntity.status(HttpStatus.OK).body(responseVideo);
    }
}
