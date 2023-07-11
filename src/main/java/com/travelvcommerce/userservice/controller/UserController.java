package com.travelvcommerce.userservice.controller;

import com.travelvcommerce.userservice.dto.UserDto;
import com.travelvcommerce.userservice.dto.request.LoginRequestDto;
import com.travelvcommerce.userservice.dto.request.RegisterRequestDto;
import com.travelvcommerce.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-service/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        userService.registerUser(registerRequestDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDto userDto) {
        userService.updateUser(userId, userDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        userService.login(loginRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/password")
    public ResponseEntity<?> findPassword(@PathVariable String userId) {
        userService.findPassword(userId);
        return ResponseEntity.ok().build();
    }

    //move to login.html
    @GetMapping("/home")
    public String home() {
        return "login";
    }


    //@GetMapping("/oauth2/authorization/google") Spring Security가 제공함.
}
