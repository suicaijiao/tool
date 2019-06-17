package com.example.service;

import java.util.List;

import com.example.config.utils.PageResult;
import com.example.entity.Demo;

public interface DemoService {

	public PageResult queryPage(String name, int currentPage,int pageSize);
	public void save(Demo demo);
	public List<Demo> linkName(String name);

}
