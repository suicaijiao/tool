package com.yinlian.cfra.study.threadtest.atomic.unsafe;

/**
 * @description 比较替换，cpu指令，硬件级别，原子操作
 * 比较替换
 * @author: suicaijiao
 * @create: 2020-06-17 20:49
 **/
public class CounterTest {

    public static void main(String[] args) throws InterruptedException {
        final CounterUnsafe counterUnsafe = new CounterUnsafe();
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    counterUnsafe.add();
                }
                System.out.println("done....");

            }).start();
        }

        Thread.sleep(6000L);
        System.out.println(counterUnsafe.i);
    }

}
