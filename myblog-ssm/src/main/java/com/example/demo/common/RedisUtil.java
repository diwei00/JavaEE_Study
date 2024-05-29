package com.example.demo.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/29 14:30
 */

@Component
public class RedisUtil {

   private static RedisTemplate<String, String> redisTemplate;

   @Autowired
   public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
      RedisUtil.redisTemplate = redisTemplate;
   }

   public void setValue(String key, String value) {
      redisTemplate.opsForValue().set(key, value);
   }



   public String getValue(String key) {
      return redisTemplate.opsForValue().get(key);
   }

   /**
    * 尝试设置键值对，如果键不存在则设置成功，存在则不执行任何操作并返回false。
    *
    * @param key   键
    * @param value 值
    * @return true如果设置成功，false如果键已存在未进行设置
    */
   public static boolean setKeyIfAbsent(String key, String value, Integer timeout, TimeUnit timeUnit) {
      return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
   }
}
