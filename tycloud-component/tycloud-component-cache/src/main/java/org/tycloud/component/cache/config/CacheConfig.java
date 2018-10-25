package org.tycloud.component.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.tycloud.component.cache.Redis;

import javax.annotation.Resource;

/**
 * Created by yaohelang on 2018/7/1.
 */
@Configuration
public class CacheConfig {

    @Resource
    public RedisTemplate redisTemplate( RedisTemplate<String, Object> redisTemplate)
    {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        Redis.redisTemplate = redisTemplate;
        return redisTemplate;
    }
}
