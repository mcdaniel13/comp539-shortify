package com.example.server.config;

import com.example.server.model.PregenUrl;
import com.example.server.model.Url;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    // Setting up the Lettuce connection factory.
    @Bean
    LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("10.70.173.51", 6379);
        return new LettuceConnectionFactory(config);
    }

    // Setting up the Redis template object.
    @Bean
    public RedisTemplate<String, Url> redisTemplateForUrls() {
        final RedisTemplate<String, Url> redisTemplateForUrls = new RedisTemplate<String, Url>();
        redisTemplateForUrls.setConnectionFactory(this.redisConnectionFactory());
        return redisTemplateForUrls;
    }
}