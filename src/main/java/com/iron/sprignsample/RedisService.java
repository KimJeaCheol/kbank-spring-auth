package com.iron.sprignsample;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveToken(String ci, String token, Duration ttl) {
        redisTemplate.opsForValue().set(ci, token, ttl);
    }

    public String getToken(String ci) {
        return redisTemplate.opsForValue().get(ci);
    }
}