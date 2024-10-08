package com.touear.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 分页工具类
 */
public class PageUtil implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** 总记录数 */
	private int totalCount;
	/** 每页记录数 */
	private int pageSize;
	/** 总页数 */
	private int totalPage;
	/** 当前页数 */
	private int currPage;
	/** 当前行数 */
	private int offset;
	/** 列表数据 */
	private List<?> list;
	
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 */
	public PageUtil(List<?> list, int totalCount) {
		this.list = list;
		this.totalCount = totalCount;
	}
	
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param offset      当前行数
	 */
	public PageUtil(List<?> list, int totalCount, int pageSize, int offset) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = offset/pageSize + 1;
		this.offset = offset;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
	
}
