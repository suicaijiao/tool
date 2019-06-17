package com.example.entity;

import java.io.Serializable;
import java.util.Date;

public class Demo implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private long id;
	private String name;
	private Date createDate;
	private Date updateDate;
	
	/**
	 * 
	 * @return
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 
	 * @param createDate
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
