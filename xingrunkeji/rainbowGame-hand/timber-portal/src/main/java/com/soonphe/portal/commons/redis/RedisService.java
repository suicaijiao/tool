package com.soonphe.portal.commons.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {

    void set(String key, String value);

    void set(String key, String value, long expire);

    <T> void setList(String key, List<T> list);

    <T> void setList(String key, List<T> list, long expire);

    <T> void setMap(String key, Map map);

    <T> void setMap(String key, Map map, long expire);

    <T> void setObject(String key, T object);

    <T> void setObject(String key, T object, long expire);

    String get(String key);

    <T> List<T> getList(String key, Class<T> clz);

    /**
     * 获取指定开始到结束之间的数据
     *
     * @param key
     * @param start
     * @param end
     * @param clz
     * @param <T>
     * @return
     */
    <T> List<T> lGet(String key, long start, long end, Class<T> clz);

    <T> Map getMap(String key, Class<T> clz);

    <T> T getObject(String key, Class<T> clz);

    boolean setExpire(String key, long expire);

    long getExpire(String key);

    void del(String... key);

    boolean exists(String key);

    boolean hasKey(String key);

    boolean flushDB();

    Set<String> keys(String pattern);

    boolean keysDel(String key);

    boolean lSet(String key, List<Object> value);

    boolean lSet(String key, Object value);

    /**
     * 自增
     *
     * @param key
     * @return
     */
    Long incr(String key);

    /**
     * 设定自增初始值
     *
     * @param key
     * @param indexValue
     */
    void setInitialIncr(String key, int indexValue);

    /**
     * 向集合左边插入缓存
     *
     * @param key
     * @param value
     * @return
     */
    long leftPushToList(final String key, Object value);

    /**
     * 向集合右边插入缓存
     *
     * @param key
     * @param value
     * @return
     */
    long rightPushToList(String key, Object value);


    /**
     * 修建指定的数据，保留开始到结束之间的数据
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    void trim(String key, long start, long end);

    /**
     * 指定区域数据
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 批量操作
     *
     * @param map
     * @param seconds
     */
    void executePipelined(Map map, long seconds);

}
