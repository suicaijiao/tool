package com.soonphe.portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.*;

/**
 * @author: soonphe
 * @date: 2019/10/14
 * @description: 定时任务配置
 */
@Configuration
@EnableScheduling
/**
 * 多线程定时任务 1.Configuration配置多线程 implements SchedulingConfigurer
 */
public class SpringTaskConfig {


    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // task to run goes here
                System.out.println("");
                service.shutdown();        //关闭任务
            }
        };

        service.scheduleAtFixedRate(runnable, 10000 / 1000, 1, TimeUnit.SECONDS);
    }
}