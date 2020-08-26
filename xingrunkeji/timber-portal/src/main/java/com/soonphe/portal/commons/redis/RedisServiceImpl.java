package com.soonphe.portal.commons.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 默认没有有效期
 */
@Service
public class RedisServiceImpl implements RedisService {
    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 解决Redis乱码问题
     *
     * @param redisTemplate
     */
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    private String cacheKey(String key) {
        return String.format("%s:%s", RedisSettings.TIMBER_PORTAL, key);
    }
    //region common

    /**
     * 指定缓存失效时间
     *
     * @param key    键
     * @param expire 时间(秒)
     * @return
     */
    @Override
    public boolean setExpire(final String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        String cacheKey = cacheKey(key);
        return redisTemplate.getExpire(cacheKey, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        String cacheKey = cacheKey(key);
        return redisTemplate.hasKey(cacheKey);
    }

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    @Override
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(cacheKey(key[0]));
            } else {
                Collection<String> keys = CollectionUtils.arrayToList(key);
                keys.forEach(k -> cacheKey(k));

                redisTemplate.delete(keys);
            }
        }
    }
    //endregion


    //region String

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public void set(String key, String value) {
        set(key, value, RedisSettings.NOT_EXPIRE);
    }

    /**
     * 设置缓存，待过期时间
     *
     * @param key
     * @param value
     * @param expire 过期时间
     * @return
     */
    @Override
    public void set(String key, String value, long expire) {

        String newKey = cacheKey(key);
        if (expire > 0) {
            redisTemplate.opsForValue().set(newKey, value, expire, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(newKey, value);
        }
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    @Override
    public String get(final String key) {
        String newKey = cacheKey(key);
        Object result = redisTemplate.opsForValue().get(newKey);
        return result != null ? result.toString() : null;
    }

    //endregion

    //region String to Object

    /**
     * 保存list，将List转为json保存
     *
     * @param key
     * @param list
     * @param <T>
     * @return
     */
    @Override
    public <T> void setList(String key, List<T> list) {

        setList(key, list, RedisSettings.NOT_EXPIRE);
    }

    @Override
    public <T> void setList(String key, List<T> list, long expire) {

        set(key, JSON.toJSONString(list), expire);
    }

    /**
     * 保存Map，将Map转为json保存
     *
     * @param key
     * @param map
     * @param <T>
     */
    @Override
    public <T> void setMap(String key, Map map) {
        setMap(key, map, RedisSettings.NOT_EXPIRE);
    }

    @Override
    public <T> void setMap(String key, Map map, long expire) {
        set(key, JSON.toJSONString(map, SerializerFeature.WriteDateUseDateFormat), expire);
    }

    /**
     * 获取list，将json字符串转为List
     *
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> getList(String key, Class<T> clz) {
        String json = get(key);
        if (json != null) {
            List<T> list = JSON.parseArray(json, clz);
            return list;
        }
        return null;
    }

    /**
     * 获取Map，将json字符串转为Map
     *
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    @Override
    public <T> Map getMap(String key, Class<T> clz) {
        String json = get(key);
        if (json != null) {
            Map map = (Map) JSON.parseObject(json, clz);
            return map;
        }
        return null;
    }

    @Override
    public <T> void setObject(String key, T object) {
        setObject(key, object, RedisSettings.NOT_EXPIRE);
    }

    @Override
    public <T> void setObject(String key, T object, long expire) {

        set(key, JSON.toJSONString(object), expire);
    }

    @Override
    public <T> T getObject(String key, Class<T> clz) {
        String json = get(key);
        if (json != null) {
            T object = JSON.parseObject(json, clz);
            return object;
        }
        return null;
    }


    //endregion


    /**
     * 是否存在
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(final String key) {
        String newKey = cacheKey(key);

        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.exists(serializer.serialize(newKey));
            }
        });
    }

    /**
     * 清空缓存
     *
     * @return
     */
    @Override
    public boolean flushDB() {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return true;
            }
        });
    }

    /**
     * 查询key批量删除
     *
     * @param key
     * @return
     */
    @Override
    public boolean keysDel(String key) {
        Collection<String> keys = redisTemplate.keys(cacheKey(key));
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
        return true;
    }

    /**
     * 获取key
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(final String pattern) {
        Set<String> result = redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();

                Set<byte[]> keys = connection.keys(serializer.serialize(pattern));
                if (keys != null && keys.size() > 0) {
                    Set<String> set = new HashSet<String>();
                    for (byte[] bytes : keys) {
                        set.add(serializer.deserialize(bytes));
                    }
                    return set;
                }

                return null;
            }
        });
        return result;
    }

    /**
     * redis自增
     *
     * @param key
     * @return
     */
    @Override
    public Long incr(String key) {
        String newKey = cacheKey(key);

        RedisAtomicLong entityIdCounter = new RedisAtomicLong(newKey, redisTemplate.getConnectionFactory());

        Long increment = entityIdCounter.getAndIncrement();

        return increment;
    }

    /**
     * 设定自增初始值
     *
     * @param key
     * @param indexValue
     */
    @Override
    public void setInitialIncr(String key, int indexValue) {
        String newKey = cacheKey(key);
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(newKey, redisTemplate.getConnectionFactory(), indexValue);
    }


    //region Map

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                setExpire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                setExpire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }


    //endregion

    //region Set

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) setExpire(key, time);
            return count;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    //endregion

    //region List

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public <T> List<T> lGet(String key, long start, long end, Class<T> clz) {
        try {
            String result = redisTemplate.opsForValue().get(key, start, end);
            if (result != null) {
                List<T> list = JSON.parseArray(result, clz);
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(cacheKey(key), value);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        String cacheKey = cacheKey(key);
        try {
            redisTemplate.opsForList().rightPush(cacheKey, value);
            if (time > 0) setExpire(key, time);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) setExpire(key, time);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        String cacheKey = cacheKey(key);
        try {
            redisTemplate.opsForList().set(cacheKey, index, value);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        String cacheKey = cacheKey(key);
        try {
            Long remove = redisTemplate.opsForList().remove(cacheKey, count, value);
            return remove;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 向集合左边插入缓存
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public long leftPushToList(String key, Object value) {
        return redisTemplate.opsForList().leftPush(cacheKey(key), value);
    }

    @Override
    public long rightPushToList(String key, Object value) {
        return 0;
    }

    /**
     * 向集合右边插入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public long rightPushToList(String key, String value) {
        return redisTemplate.opsForList().rightPush(cacheKey(key), value);
    }


    @Override
    public void trim(String key, long start, long end) {
        redisTemplate.opsForList().trim(cacheKey(key), start, end);
    }


    @Override
    public List<Object> lRange(String key, long start, long end) {
        try {
            List<Object> result = redisTemplate.opsForList().range(cacheKey(key), start, end);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    /**
     * 批量操作
     *
     * @param map
     * @param seconds
     */
    public void executePipelined(Map map, long seconds) {
        RedisSerializer serializer = redisTemplate.getStringSerializer();
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                map.forEach((key, value) -> {
                    connection.set(serializer.serialize(key), serializer.serialize(value), Expiration.seconds(seconds), RedisStringCommands.SetOption.UPSERT);
                });
                return null;
            }
        }, serializer);
    }


    //endregion
}

