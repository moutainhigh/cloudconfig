package com.xkd.model;

/**
 * 分页数据展示
 * pageSize 每页条数
 * pageNo第几页
 * totalPage总页数
 * totalCount总条数
 * @author fsj
 *
 */
public class ResponsePage extends ResponseDbCenter {
	
	
	private int pageSize;

	private int pageNo;

	private int totalPage;
	
	private int totalCount;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTcount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
	public ResponsePage(int pageNo, int pageSize, int count) {
		this.setPageNo(pageNo);
		this.setPageSize(pageSize);
		this.setTotalCount(count);
		int totalPage = 0;
		if (count % pageSize == 0) {
			totalPage = count / pageSize;
		} else {
			totalPage = count / pageSize + 1;
		}
		this.setTotalPage(totalPage);
	}

}
