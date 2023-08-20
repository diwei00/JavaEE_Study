package com.example.springredis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class RedisController {


    // 用来操作文本数据
    @Autowired
    private StringRedisTemplate redisTemplate;


    @GetMapping("/string")
    public String testString() {

       String result = redisTemplate.execute((RedisConnection connection) -> {

            connection.flushAll();
            redisTemplate.opsForValue().set("key3", "333");
            String result2 = redisTemplate.opsForValue().get("key3");
            return result2;
        });
        System.out.println(result);


        redisTemplate.opsForValue().set("key", "111");
        redisTemplate.opsForValue().set("key1", "222");
        redisTemplate.opsForValue().set("key2", "333");

        String tmp = redisTemplate.opsForValue().get("key");
        System.out.println(tmp);
        return "OK";

    }

    @GetMapping("/list")
    public String testList() {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.flushAll();
            return null;
        });
        redisTemplate.opsForList().leftPushAll("key", "1", "2", "3", "4");
        String value = redisTemplate.opsForList().leftPop("key");
        System.out.println(value);
        return "OK";

    }

    @GetMapping("/hash")
    public String testHash() {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.flushAll();
            return null;
        });
        redisTemplate.opsForHash().put("key", "f1", "v1");
        redisTemplate.opsForHash().put("key", "f2", "v2");
        redisTemplate.opsForHash().put("key", "f3", "v1");

        String value = (String) redisTemplate.opsForHash().get("key", "f2");
        Set<Object> set = redisTemplate.opsForHash().keys("key");
        Long count = redisTemplate.opsForHash().size("key");
        System.out.println(value);
        System.out.println(set);
        System.out.println(count);
        return "OK";

    }

    @GetMapping("/set")
    public String testSet() {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.flushAll();
            return null;
        });
        redisTemplate.opsForSet().add("key", "1", "2", "3");
        Set<String> set = redisTemplate.opsForSet().members("key");
        Boolean exists = redisTemplate.opsForSet().isMember("key", "3");
        String tmp = redisTemplate.opsForSet().pop("key");
        Long size = redisTemplate.opsForSet().size("key");
        System.out.println(set);
        System.out.println(exists);
        System.out.println(tmp);
        System.out.println(size);
        return "OK";

    }

    @GetMapping("/zset")
    public String testZSet() {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.flushAll();
            return null;
        });
        redisTemplate.opsForZSet().add("key", "111", 10);
        redisTemplate.opsForZSet().add("key", "222", 20);
        redisTemplate.opsForZSet().add("key", "333", 30);
        Set<String> set = redisTemplate.opsForZSet().range("key", 0, -1);
        Long rank = redisTemplate.opsForZSet().rank("key", "222");
        Double score = redisTemplate.opsForZSet().score("key", "222");
        Long count = redisTemplate.opsForZSet().zCard("key");
        ZSetOperations.TypedTuple<String> tmp = redisTemplate.opsForZSet().popMax("key");

        System.out.println(set);
        System.out.println(rank);
        System.out.println(score);
        System.out.println(count);
        System.out.println(tmp.getValue());
        System.out.println(tmp.getScore());
        return "OK";



    }


}
