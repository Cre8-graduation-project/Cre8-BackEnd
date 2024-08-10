package com.gaduationproject.cre8.externalApi.redis.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final RedisTemplate<String,String> redisTemplate;

    public void saveAtRedis(final String key,final String value,final Long time,TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value,time, timeUnit);
    }


    public void deleteAtRedis(final String key){
        redisTemplate.delete(key);
    }

    public String getValueByKey(final String key){
        return redisTemplate.opsForValue().get(key);
    }

}
