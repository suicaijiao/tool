package com.example.service;

import java.util.List;

import com.example.config.utils.PageResult;
import com.example.entity.Demo;

public interface DemoTestService {

	public List<Demo> linkName(String name);
	
	public Demo getById(Long id);
	
	public String getNameById(Long id);
	
	public void save(Demo demo);
	
	public int count(String name);
	
	public PageResult queryPage(String name, int currentPage,int pageSize);
	
	public void job1();
	
	public void job2();
}
