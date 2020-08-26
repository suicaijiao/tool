package com.yinlian.cfra.study.threadtest.atomic.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-06-17 17:15
 **/
public class CounterUnsafe {

    volatile int i = 0; // cas 是硬件级别的，拿到内存地址才能拿到
    private static Unsafe unsafe = null;

    private static long valueOffset;
    static {
        // jdk 是这样写，我们不可以使用这样方式直接拿
//        unsafe = Unsafe.getUnsafe();
        // 反射
        try {
           Field field = Unsafe.class.getDeclaredField("theUnsafe");
           // 可见
           field.setAccessible(true);
           // 获得unsafe
           unsafe = (Unsafe) field.get(null);

           //获取偏移量，获取的当前类的i字段
           Field fieldI = CounterUnsafe.class.getDeclaredField("i");
           valueOffset = unsafe.objectFieldOffset(fieldI);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void add(){
        // 获取当前的值
        for(;;){
            // 通过偏移量获取当前对象的值，要修改的对象是当前对象
            int current = unsafe.getIntVolatile(this,valueOffset);
            // 对当前的值进行修改，要修改的哪个对象，的哪个值，修改会失败
            if(unsafe.compareAndSwapInt(this,valueOffset,current,current+1)){
                break;
            }
        }
    }
}
