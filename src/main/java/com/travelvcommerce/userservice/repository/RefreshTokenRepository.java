package com.travelvcommerce.userservice.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private RedisTemplate<String, String> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String email, String refreshToken, long expiryTime) {
        redisTemplate.opsForValue().set(email, refreshToken, expiryTime, TimeUnit.MILLISECONDS);
    }

    public String find(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void delete(String email) {
        redisTemplate.delete(email);
    }
}
