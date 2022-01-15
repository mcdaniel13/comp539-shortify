package com.example.server.repository;

import com.example.server.model.PregenUrl;
import com.example.server.model.Url;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public class UrlRepository implements IUrlRepository {


    private RedisTemplate<String, Url> redisTemplateForUrls;
    private final HashOperations hashOperations; //to access Redis cache
    public UrlRepository(RedisTemplate<String, Url> redisTemplateForUrls) {
        this.redisTemplateForUrls = redisTemplateForUrls;
        hashOperations = redisTemplateForUrls.opsForHash();
    }

    @Override
    public void save(Url url) {
        hashOperations.put("urlMappings", url.getShortUrl(), url);
    }

    @Override
    public Map<String, Url> findAll() {
        return hashOperations.entries("urlMappings");
    }

    @Override
    public Url findById(String id) {
        return (Url)hashOperations.get("urlMappings", id);
    }

    @Override
    public void update(Url url) {
        save(url);
    }

    @Override
    public void delete(String id) {
        hashOperations.delete("urlMappings", id);
    }
}
