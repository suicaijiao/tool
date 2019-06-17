package com.example.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;

import com.github.pagehelper.PageHelper;

/**
 * 分页插件，不好用
* @Description:
* @author suicaijiao  
* @date 2019年4月12日
 */
public class MyBatisConfiguration {
	@Bean
	public PageHelper pageHelper(){
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("offsetAsPageNum", "true");
		properties.setProperty("rowBoundsWithCount", "true");
		properties.setProperty("reasonable", "true");
		pageHelper.setProperties(properties);
		return pageHelper;
		
	}

}
