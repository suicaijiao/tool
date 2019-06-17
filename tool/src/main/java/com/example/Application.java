package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;

import com.example.config.SpringBeanFactoryUtils;

/**
 * @MapperScan("com.example.*mapper")
 * 扫描该包下的class,主要是MyBatis的持久化类
 * @author suicaijiao
 * @EnableJms activemq
 * @EnableCaching redis 缓存注解
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.example.dao"})
@EnableCaching
@EnableJms
@ComponentScan
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public SpringBeanFactoryUtils springBeanFactoryUtils(){
		SpringBeanFactoryUtils springBeanFactoryUtils = new SpringBeanFactoryUtils();
		return springBeanFactoryUtils;
	}
}
