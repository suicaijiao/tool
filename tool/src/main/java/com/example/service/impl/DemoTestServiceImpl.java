package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.config.utils.PageResult;
import com.example.dao.DemoDao;
import com.example.entity.Demo;
import com.example.service.DemoService;
import com.example.service.DemoTestService;

@Service("demoTestService")
public class DemoTestServiceImpl implements DemoTestService {

	@Autowired
	private DemoDao demoDao;
	@Reference
	private DemoService demoService;

	@Override
	public Demo getById(Long id) {
		return null;
	}

	@Override
	public String getNameById(Long id) {
		return demoDao.getNameById(id);
	}

	
	@Override
	public int count(String name) {
		return demoDao.count(name);
	}

	@Override
	//@Cacheable(value="queryPage")
	public PageResult queryPage(String name, int currentPage, int pageSize) {
		return demoService.queryPage(name, currentPage, pageSize);
	}

	@Override
	public void save(Demo demo) {
		demoService.save(demo);
	}
	
	@Override
	public List<Demo> linkName(String name) {
		return demoService.linkName(name);
	}
	
	
	@Override
	public void job1() {
		System.out.println("每5秒执行一次");
		
	}
	
	
	@Override
	public void job2() {
		System.out.println("每10秒执行一次");
		
	}
}
