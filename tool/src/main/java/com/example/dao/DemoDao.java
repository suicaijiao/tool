package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.entity.Demo;

public interface DemoDao {
	
	public int save(Demo demo);
	
	public int update(Demo demo);
	
	public int delet(Integer id);
	
	public int deletByIs(List<Integer> ids);
	
	public Demo getById(Integer id);
	
	public List<Demo> getAll(Demo demo);
	
	public int count(Demo demo);
	
	
	
	
	public List<Demo> linkName(String name);
	
	
	public String getNameById(Long id);
	
	public int count(@Param("name")String name);
	
	public List<Demo> demoPageList(
			@Param("name")String name,
			@Param("currentPage") int currentPage,
			@Param("pageSize") int pageSize
			);
}
