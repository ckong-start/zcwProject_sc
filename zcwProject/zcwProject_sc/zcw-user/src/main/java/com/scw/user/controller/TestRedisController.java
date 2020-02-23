package com.scw.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @create 2020-02-08 22:40
 */
@RestController
public class TestRedisController {
    Logger logger = LoggerFactory.getLogger(TestRedisController.class);
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/testRedis")
    public String testRedis(){
        Object key1 = redisTemplate.opsForValue().get("key1");
        logger.info("key1的值为：{}", key1);
        //redisTemplate.opsForValue().set("key1", "hehe");

        //stringRedisTemplate.opsForValue().set("key2", "haha");
        String key2 = stringRedisTemplate.opsForValue().get("key2");
        logger.warn("key2的值为：{}", key2);
        return "haha";
    }
}
