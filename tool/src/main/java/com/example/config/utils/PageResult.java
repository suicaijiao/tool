package com.example.config.utils;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

public class PageResult extends JdkSerializationRedisSerializer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 每页显示条数
	 */
	private int pageSize = 10;
	/**
	 * 当前页号
	 */
	private int currentPage = 1;
	/**
	 * 数据总数
	 */
	private int totalCount;

	/**
	 * 分页数据
	 */
	@SuppressWarnings("rawtypes")
	private List pageResult;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getPageResult() {
        return pageResult;
    }

    @SuppressWarnings("rawtypes")
	public void setPageResult(List pageResult) {
        this.pageResult = pageResult;
    }

	public boolean isFirst() {
		return getCurrentPage() <= 1;
	}

	public boolean isLast() {
		return getCurrentPage() >= getTotalPage();
	}

	public int getStartRecord() {
		if ((currentPage - 1) * pageSize < 0) {
			return 0;
		}
		return (currentPage - 1) * pageSize;
	}

	public int getTotalPage() {
		return totalCount / getPageSize() * getPageSize() == totalCount ? totalCount / getPageSize()
				: totalCount / getPageSize() + 1;
	}

}
