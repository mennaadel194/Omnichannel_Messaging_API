package com.omnichannel.messagingplatform.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import io.github.bucket4j.*;
import redis.clients.jedis.Jedis;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RateLimiterService {

    private static final int MAX_MESSAGES_PER_MINUTE = 3;
    private static final Duration RATE_LIMIT_WINDOW = Duration.ofMinutes(1);

    private final StringRedisTemplate redisTemplate;


    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean canSendMessage(Long userId) {
        String key = RATE_LIMIT_KEY_PREFIX + userId;

        Bucket bucket = Bucket4j.extension(RedisBucketBuilder.class)
                .build(redisTemplate, key, 1, RATE_LIMIT_WINDOW, MAX_MESSAGES_PER_MINUTE);

        // Check if the user can still send a message within the rate limit
        return bucket.tryConsume(1);  // Consume 1 token for each message
        return true;
    }
}
