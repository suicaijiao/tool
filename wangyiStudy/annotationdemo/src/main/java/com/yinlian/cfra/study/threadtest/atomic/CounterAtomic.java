package com.yinlian.cfra.study.threadtest.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-06-17 16:16
 **/
public class CounterAtomic {

    // 原子操作
    AtomicInteger i = new AtomicInteger(0);


    public void add() {
        i.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        final CounterAtomic counterUnsafe = new CounterAtomic();
        for(int i=0;i<6;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j=0;j<10000;j++){
                        counterUnsafe.add();
                    }
                    System.out.println("done....");
                }
            }).start();
        }

        Thread.sleep(6000L);
        System.out.println(counterUnsafe.i);
    }
}
