package com.soonphe.portal.util.redis;

import com.soonphe.portal.component.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: soonphe
 * @date: 2019/10/17
 * @description:
 */
public class RedisUtil<K extends Serializable, V extends Serializable> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    private static RedisTemplate redisTemplate = SpringContextHolder.getBean("redisTemplate");

    static {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(new SerializeUtil());
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(new SerializeUtil());
    }

    public static void set(final String key, Object value) {

        redisTemplate.opsForValue().set(key, value);
    }

    public static void set(final String key, Object value, final Duration l) {
        redisTemplate.opsForValue().set(key, value, l);
    }

    public static <T> T get(final String key) {
        return (T) redisTemplate.opsForValue().get(key);

    }

    public static void pushListValue(final String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public static Object rightPullToList(final String key, final long l) {
        if (l > 0) {
            return redisTemplate.opsForList().rightPop(key, l, TimeUnit.SECONDS);
        }
        return redisTemplate.opsForList().rightPop(key);
    }

    public static Object rightPopAndLeftPush(final String key, String value) {
        return redisTemplate.opsForList().rightPopAndLeftPush(key, value);
    }


    public static void setList(final String key, List<Object> value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }


    public static Long getListSize(final String key) {
        return redisTemplate.opsForList().size(key);
    }


    public static Object getListIndexValue(final String key, final long l) {
        return redisTemplate.opsForList().index(key, l);
    }


    public static List<Object> getList(final String key) {

        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        Long size = listOperations.size(key);
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Object obj = listOperations.rightPop(key);
            list.add(obj);
        }
        return list;
    }

    public static void putMapValue(final String key, final String keyMap, Object valueMap) {
        redisTemplate.opsForHash().put(key, keyMap, valueMap);
    }

    public static void setMap(final String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }


    public static Object getMap(final String key, final String keyMap) {
        return redisTemplate.opsForHash().get(key, keyMap);

    }


    public static Map<String, Object> getMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

}
