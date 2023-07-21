package com.travelvcommerce.contentslaveservice.controller;

import com.travelvcommerce.contentslaveservice.dto.VideoDto;
import com.travelvcommerce.contentslaveservice.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("content-slave-service")
public class DatabaseController {
    @Autowired
    DatabaseService databaseService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody VideoDto videoDto) {
        try {
            databaseService.create(videoDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PutMapping("/update/{videoId}")
    public ResponseEntity update(@PathVariable(name = "videoId") String videoId, @RequestBody VideoDto videoDto) {
        try {
            databaseService.update(videoId, videoDto);
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/delete/{videoId}")
    public void delete(@RequestBody VideoDto videoDto) {
    }
}
