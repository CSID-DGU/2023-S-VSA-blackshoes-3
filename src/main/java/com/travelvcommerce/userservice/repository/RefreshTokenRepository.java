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

    public void save(String userType, String email, String refreshToken, long expiryTime) {
        String key = userType + ":" + email;
        redisTemplate.opsForValue().set(key, refreshToken, expiryTime, TimeUnit.MILLISECONDS);
    }

    public String find(String userType, String email) {
        String key = userType + ":" + email;
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshTokenByUserEmail(String userType, String email) {
        String key = userType + ":" + email;
        redisTemplate.delete(key);
    }
}
