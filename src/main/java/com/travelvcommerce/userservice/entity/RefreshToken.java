package com.travelvcommerce.userservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("RefreshToken")
@Getter
@Setter
public class RefreshToken {

    @Id
    private String id;
    private String username;

}