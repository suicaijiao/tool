package com.yinlian.cfra.study.threadtest.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-06-15 10:45
 **/
public class Demo6 {
    /**
     * threadLocal变量，每个线程都有一个副本，互不干扰
     */
    public static ThreadLocal<String> value = new ThreadLocal<>();

    public static Object baozidian = null;

    public void threadLocalTest() throws Exception {
        // 主线程设置值
        value.set("这里是线程设置的123");
        String v = value.get();
        System.out.println("线程1执行之前，主线程的值：" + v);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String v = value.get();
                System.out.println("线程1取到的值：" + v);
                value.set("这里是线程1设置的456");
                v = value.get();

                System.out.println("重设置之后，线程1取到的值：" + v);
                System.out.println("线程1执行结束");
            }
        }).start();

        Thread.sleep(5000L);

        v = value.get();
        System.out.println("线程1执行之后，主线程取到的值：" + v);
    }

    /**
     * 被弃用，弃用的原因是容易写出死锁的代码
     * @throws Exception
     */
    public void suspendResumeTest() throws Exception{
        Thread consumerThread = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1.没包子，进入等待");
                Thread.currentThread().suspend();
            }
            System.out.println("2.买到包子，回家");
        });

        consumerThread.start();

        // 3秒之后，生产一个包子
        Thread.sleep(3000L);

        baozidian = new Object();
        consumerThread.resume();
        System.out.println("3.通知消费者");
    }

    /**
     * suspend挂起之后并不会释放锁，故此容易写出死锁代码
     */
    public void suspendResumeDeadLockTest() throws Exception{
        // 启动线程
        Thread consumerThread = new Thread(()->{
           if(baozidian ==null){
               System.out.println("1.没包子，进入等待");
               // 当线程拿到锁，让后挂起
               synchronized (this){
                   Thread.currentThread().suspend();
               }
           }
            System.out.println("2.买到包子，回家");
        });

        consumerThread.start();

        // 3秒后生产一个包子
        Thread.sleep(3000);
        baozidian = new Object();
        // 争取到锁以后，在恢复consumerThread
        synchronized (this){
            consumerThread.resume();
        }
        System.out.println("3.通知消费者");
    }

    /**
     * 导致程序永久挂起
     */
    public void suspendResumeDeadLockTest2() throws Exception{
        // 启动线程
        Thread consumerThread = new Thread(()->{
            if(baozidian ==null){
                System.out.println("1.没包子，进入等待");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 当线程拿到锁，让后挂起
                Thread.currentThread().suspend();
            }
            System.out.println("2.买到包子，回家");
        });

        consumerThread.start();

        // 3秒后生产一个包子
        Thread.sleep(3000);
        baozidian = new Object();
        // 争取到锁以后，在恢复consumerThread
        consumerThread.resume();
        System.out.println("3.通知消费者");
    }

    /**
     * 正常的wait/notify 必须在同步代码块执行，通过监视器实现的
     * @throws Exception
     */
    public void waitNotifyTest() throws Exception{
        // 启动线程
        new Thread(()->{
            if(baozidian ==null){
                synchronized (this){
                    System.out.println("1.进入等待");
                    try {

                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2.买到包子，回家");
        }).start();

        // 三秒以后生产一个包子
        Thread.sleep(3000);
        baozidian = new Object();
        synchronized (this){
            this.notifyAll();
            System.out.println("3.通知消费者");
        }
    }

    /**
     * 会导致程序永久等待的 wait/notify
     * 要注意顺序，休眠5秒以后锁无法获取，导致死锁
     * 类似坐火车，乘客进站上车 ，休眠5秒相当于车走了才进站
     * @throws Exception
     */
    public void waitNotifyDeadLockTest() throws Exception{
        // 启动线程
        new Thread(()->{
            if(baozidian ==null){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (this){
                    System.out.println("1.进入等待");
                    try {

                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2.买到包子，回家");
        }).start();

        // 三秒以后生产一个包子
        Thread.sleep(3000);
        baozidian = new Object();
        synchronized (this){
            this.notifyAll();
            System.out.println("3.通知消费者");
        }
    }

    /**
     * 正常的park/unpark
     */
    public void parkUnparkTest()throws Exception{
        // 启动线程
        Thread consumerThread = new Thread(()->{
           if(baozidian == null){
               System.out.println("1.进入等待");
               // 挂起当前线程
               LockSupport.park();
           }
            System.out.println("2.买到包子，回家");
        });

        consumerThread.start();
        // 三秒以后生产一个包子
        Thread.sleep(3000);
        baozidian = new Object();
        // 指定线程继续执行，颁发许可证
        LockSupport.unpark(consumerThread);
        System.out.println("3.通知消费者");
    }

    /**
     * 死锁的park/unpark
     */
    public void parkUnparkDeadLockTest()throws Exception{
        // 启动线程
        Thread consumerThread = new Thread(()->{
            if(baozidian == null){
                System.out.println("1.进入等待");
                // 挂起当前线程
                synchronized (this){
                    LockSupport.park();
                }

            }
            System.out.println("2.买到包子，回家");
        });

        consumerThread.start();
        // 三秒以后生产一个包子
        Thread.sleep(3000);
        baozidian = new Object();
        // 指定线程继续执行，颁发许可证
        synchronized (this){
            LockSupport.unpark(consumerThread);
        }
        System.out.println("3.通知消费者");
    }




    public static void main(String[] args) throws Exception {
        new Demo6().threadLocalTest();
//        new Demo6().suspendResumeTest();
//        new Demo6().suspendResumeDeadLockTest();
//        new Demo6().suspendResumeDeadLockTest2();
//        new Demo6().waitNotifyTest();
//        new Demo6().waitNotifyDeadLockTest();
//        new Demo6().parkUnparkTest();
//        new Demo6().parkUnparkDeadLockTest();
    }


}
