package com.soonphe.portal.commons.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Created by Wang Shibiao on 2017/9/13.
 */
@Component
public class RedisUtil {
    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 缓存Hash数据
     */
    public boolean setHashObject(String k, Map<String,String> v){
    	try {
    		redisTemplate.opsForHash().putAll(initKey(k), v);
    		return true;
    	} catch (Throwable t) {
    		logger.error(t.getMessage());
    	}
    	return false;
    }

    /**
     * lzz
     * 获取Hash缓存
     * @param k
     * @return
     */
    public Map<Object, Object> getHashObject(String k) {
        try {
            Map<Object, Object> resultMap = redisTemplate.opsForHash().entries(initKey(k));
            return resultMap;
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
        return null;
    }
    
    /**
     * 缓存List数据
     */
    public boolean setListObject(String k, String v){
    	try {
    		redisTemplate.opsForList().rightPush(initKey(k), v);
    		return true;
    	} catch (Throwable t) {
    		logger.error(t.getMessage());
    	}
    	return false;
    }

    /**
     * lzz
     * 获取List缓存
     * @param k
     * @return
     */
    public List<Object> getListObject(String k) {
        try {
           	List<Object> list = redisTemplate.opsForList().range(initKey(k),0,-1);
            return list;
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
        return null;
    }
    
    /**
     * set Object操作
     * @param k
     * @param v
     * @param time 单位：秒
     * @return
     */
    public boolean setObject(String k, Object v, long time) {
        try {
            k = initKey(k);
            this.setObject(k, v);
            if (time > 0)
                redisTemplate.expire(k, time, TimeUnit.SECONDS);

            return true;
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }

        return false;
    }
    /**
     * set Object操作
     * @param k
     * @param v
     * @param time 单位：秒
     * @return
     */
    public boolean setString(String k, String v, long time) {
        try {
            k = initKey(k);
            this.setString(k, v);
            if (time > 0) {
                redisTemplate.expire(k, time, TimeUnit.SECONDS);
            }

            return true;
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }

        return false;
    }

    private String initKey(String k){
        if(null != k){
            k = k.toLowerCase();
        }
        return k;
    }
    /**
     * set Object操作
     * @param k
     * @param v
     * @return
     */
    public boolean setObject(String k, Object v) {
        try {
            k = initKey(k);
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(k, v,1,TimeUnit.DAYS);
            return true;
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
        return false;
    }

    /**
     * set Object操作
     * @param k
     * @param v
     * @return
     */
    public boolean setString(String k, String v) {
        try {
            k = initKey(k);
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(k, v,1,TimeUnit.DAYS);
            return true;
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
        return false;
    }
    /**
     * 获取缓存
     * @param k
     * @return
     */
    public Object getObject(String k) {
        try {
            k = initKey(k);
            ValueOperations<String, Object> valueOperations =  redisTemplate.opsForValue();
            return valueOperations.get(k);
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
        return null;
    }
    /**
     * 获取缓存
     * @param k
     * @return
     */
    public String getString(String k) {
        try {
            k = initKey(k);
            ValueOperations<String, Object> valueOperations =  redisTemplate.opsForValue();
            return String.valueOf(valueOperations.get(k));
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
        return null;
    }

    /**
     * 移除缓存
     * @param key
     * @return
     */
    public boolean delete(String key) {
        try {
            redisTemplate.delete(initKey(key));
            return true;
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
        return false;
    }

    /** 批量删除key
     * @param pattern
     */
    public void deletePattern(String pattern) {
        Set<String> keys = redisTemplate.keys(initKey(pattern));
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 是否有key
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(initKey(key));
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
        return false;
    }
}
