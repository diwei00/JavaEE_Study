package com.example.chatroom.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    private HashOperations hashOperations;
    private ListOperations listOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
        listOperations = redisTemplate.opsForList();
    }

    // String 类型操作

    /**
     * 设置String类型数据
     * @param key 键
     * @param value 值
     */
    public void setString(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置String类型数据并设置过期时间
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void setString(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取String类型数据
     * @param key 键
     * @return 值
     */
    public Object getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除String类型数据
     * @param key 键
     */
    public void deleteString(String key) {
        redisTemplate.delete(key);
    }

    // Hash 类型操作

    /**
     * 设置Hash类型数据
     * @param key 键
     * @param hashKey hash键
     * @param value 值
     */
    public void setHash(String key, Object hashKey, Object value) {
        hashOperations.put(key, hashKey, value);
    }

    /**
     * 设置Hash类型数据并设置过期时间
     * @param key 键
     * @param hashKey hash键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void setHash(String key, Object hashKey, Object value, long timeout, TimeUnit unit) {
        hashOperations.put(key, hashKey, value);
        redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取Hash类型数据
     * @param key 键
     * @param hashKey hash键
     * @return 值
     */
    public Object getHash(String key, Object hashKey) {
        return hashOperations.get(key, hashKey);
    }

    /**
     * 获取Hash类型所有数据
     * @param key 键
     * @return 所有数据
     */
    public List<Object> getHashAll(String key) {
        return new ArrayList<>(hashOperations.entries(key).values());
    }

    /**
     * 删除Hash类型数据
     * @param key 键
     * @param hashKey hash键
     */
    public void deleteHash(String key, Object hashKey) {
        hashOperations.delete(key, hashKey);
    }

    /**
     * 删除整个Hash
     * @param key 键
     */
    public void deleteHash(String key) {
        redisTemplate.delete(key);
    }

    // List 类型操作

    /**
     * 向List类型数据添加元素（从右侧添加）
     * @param key 键
     * @param value 值
     */
    public void rightPush(int key, int value) {
        listOperations.rightPush(key, value);
    }

    /**
     * 向List类型数据添加元素（从右侧添加）
     * @param key 键
     * @param values 值
     */
    public void rightPushAll(Integer key, Integer... values) {
        listOperations.rightPushAll(key, values);
    }

    /**
     * 向List类型数据添加元素（从左侧添加）
     * @param key 键
     * @param value 值
     */
    public void leftPush(Integer key, Integer value) {
        listOperations.leftPush(key, value);
    }

    /**
     * 从List类型数据获取元素（从右侧弹出）
     * @param key 键
     * @return 值
     */
    public Object rightPop(Integer key) {
        return listOperations.rightPop(key);
    }


    /**
     * 从List类型数据获取元素（从左侧弹出）
     * @param key 键
     * @return 值
     */
    public Object leftPop(Integer key) {
        return listOperations.leftPop(key);
    }

    /**
     * 获取List类型数据的指定范围
     * @param key 键
     * @param start 开始索引
     * @param end 结束索引
     * @return 指定范围内的元素列表
     */
    public List<Integer> range(Integer key, long start, long end) {
        return listOperations.range(key, start, end);
    }



    /**
     * 获取List类型数据的长度
     * @param key 键
     * @return 长度
     */
    public Long size(Integer key) {
        return listOperations.size(key);
    }

    /**
     * 删除List类型数据中的指定元素
     * @param key 键
     * @param count 删除个数（正数从左到右，负数从右到左）
     * @param value 值
     * @return 删除的元素个数
     */
    public Long remove(Integer key, long count, Object value) {
        return listOperations.remove(key, count, value);
    }

    /**
     * 设置List类型数据的指定索引位置的值
     * @param key 键
     * @param index 索引
     * @param value 值
     */
    public void set(Integer key, long index, Integer value) {
        listOperations.set(key, index, value);
    }

    // 检查key是否存在

    /**
     * 检查key是否存在
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(Object key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 指定key删除
     * @param key 键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }


    /**
     * 批量指定key删除
     * @param keys 键
     */
    public void delete(List<Integer> keys) {
        redisTemplate.delete(keys);
    }
}
