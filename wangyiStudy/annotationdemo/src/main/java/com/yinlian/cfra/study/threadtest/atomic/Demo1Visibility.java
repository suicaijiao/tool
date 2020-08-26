package com.yinlian.cfra.study.threadtest.atomic;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-06-15 17:25
 **/
public class Demo1Visibility {

    int i = 0;
    volatile boolean isRunning = true;

    public static void main(String[] args) throws InterruptedException {
        Demo1Visibility demo = new Demo1Visibility();
        new Thread(() -> {
            while (demo.isRunning) {
                demo.i++;
            }
            System.out.println(demo.i);

        }).start();

        Thread.sleep(3000L);

        demo.isRunning = false;
        System.out.println("shutdown...");
    }
}
