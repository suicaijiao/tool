package com.example.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 普通类调用Spring注解方式的Service层bean
* @Description:
* @author suicaijiao  
* @date 2019年4月11日
 */
public class SpringBeanFactoryUtils implements ApplicationContextAware {

	private static ApplicationContext appCtx;

	/*
	 * 此方法可以把ApplicationContext对象inject到当前类中作为一个静态成员变量。
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appCtx = applicationContext;
	}

	/**
	 * 获取ApplicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return appCtx;
	}

	/**
	 * 根据名称查找Bean
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return appCtx.getBean(beanName);
	}
	
	/**
	 * 根据class查找Bean
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		T obj = (T)appCtx.getBean(clazz);
		return obj;
	}
	
}
