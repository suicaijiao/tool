package com.yinlian.cfra.study.threadtest.atomic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description ReentrantLock  synchronized 属于单线程效率不好
 * @author: suicaijiao
 * @create: 2020-06-22 16:57
 **/
public class CounterLock {

    volatile int i=0;

    Lock lock = new ReentrantLock();

    public void add(){
        lock.lock();
        i++;
        lock.unlock();
    }
}
