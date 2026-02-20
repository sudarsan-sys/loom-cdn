package com.loom.cdn.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // This channel name must match what the subscriber listens to
    private static final String CHANNEL_NAME = "cdn-events";

    // ✅ This is the method CdnService is trying to call!
    public void publish(String message) {
        redisTemplate.convertAndSend(CHANNEL_NAME, message);
        System.out.println("📢 Broadcasted to Redis: " + message);
    }
}