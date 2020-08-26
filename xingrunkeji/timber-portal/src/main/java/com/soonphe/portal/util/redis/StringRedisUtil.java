package com.soonphe.portal.util.redis;

import com.soonphe.portal.component.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: soonphe
 * @date: 2019/10/17
 * @description:
 */
public class StringRedisUtil<K extends Serializable, V extends Serializable> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(StringRedisUtil.class);

    private static StringRedisTemplate stringRedisTemplate = SpringContextHolder.getBean("stringRedisTemplate");


    public static void set(final String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }


    public static void set(final String key, String value, final long l) {
        stringRedisTemplate.opsForValue().set(key, value, l);
    }


    public static void set(final String key, String value, final long l, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, l, timeUnit);
    }

    public static String get(final String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


    public static boolean contain(final String key) {
        return get(key) != null;
    }

    public static boolean expire(final String key, long expire) {
        return stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public static void del(final String key) {
        stringRedisTemplate.delete(key);
    }

    public static void flushDb() {
        stringRedisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushDb();
            return null;
        });
    }


    public static long leftPushToList(final String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }


    public static Object rightPullToList(final String key, final long l) {
        if (l > 0) {
            return stringRedisTemplate.opsForList().rightPop(key, l, TimeUnit.SECONDS);
        }
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    public static Object rightPopAndLeftPush(final String key, String value) {
        return stringRedisTemplate.opsForList().rightPopAndLeftPush(key, value);
    }


    public static void setList(final String key, List<String> list) {
        if (list.size()>0){
            stringRedisTemplate.opsForList().leftPushAll(key, list);
        }
    }


    public static Long getListSize(final String key) {
        return stringRedisTemplate.opsForList().size(key);
    }


    public static String getListIndexValue(final String key, final long l) {
        return stringRedisTemplate.opsForList().index(key, l);
    }


    public static Long removeListIndexValue(final String key, final long c, String s) {
        return stringRedisTemplate.opsForList().remove(key, c, s);
    }

    public static List<String> getList(final String key) {
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        Long size = listOperations.size(key);
        List<String> list = new ArrayList<>();
        if (size>0){
            list = listOperations.range(key,0,-1);
        }

        return list;
    }

    public static void putMapValue(final String key, final String keyMap, String valueMap) {
        stringRedisTemplate.opsForHash().put(key, keyMap, valueMap);
    }

    public static void setMap(final String key, Map<String, String> map) {
        stringRedisTemplate.opsForHash().putAll(key, map);
    }


    public static Object getMap(final String key, final String keyMap) {
        return stringRedisTemplate.opsForHash().get(key, keyMap);
    }

    /**
     * 取map中的所有对象
     *
     * @param key map的键
     * @return
     */
    public static Map<Object, Object> getMap(final String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }


}
