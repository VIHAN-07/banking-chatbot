package com.barclays.bankingchatbot.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Bean
    public Bucket createNewBucket() {
        // Allow 100 requests per minute per user
        Bandwidth limit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1)));
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    public Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, this::newBucket);
    }

    private Bucket newBucket(String key) {
        // Different limits for different types of requests
        if (key.startsWith("voice_")) {
            // Voice requests are more expensive - limit to 20 per minute
            Bandwidth limit = Bandwidth.classic(20, Refill.intervally(20, Duration.ofMinutes(1)));
            return Bucket4j.builder().addLimit(limit).build();
        } else if (key.startsWith("transfer_")) {
            // Transfer requests are critical - limit to 5 per minute
            Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
            return Bucket4j.builder().addLimit(limit).build();
        } else {
            // Default chat requests - 100 per minute
            return createNewBucket();
        }
    }
}
