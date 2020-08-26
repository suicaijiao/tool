package com.soonphe.portal.util.redis;

import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.*;

/**
 * @author: soonphe
 * @date: 2019/10/17
 * @description: 自定义序列化与反序列化——用于redisTemplate对象存取，必须继承RedisSerializer
 */
public class SerializeUtil implements RedisSerializer {

    @Override
    public byte[] serialize(Object object) {
        ObjectOutputStream oos;
        ByteArrayOutputStream baos;
        try {
            //字节数组输出流，
            baos = new ByteArrayOutputStream();
            //对象输出流——创建写入指定OutputStream的ObjectOutputStream
            oos = new ObjectOutputStream(baos);
            //将指定的对象写进字节数组输出
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream bais;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
