package com.cssrc.ibms.core.db.mybatis.page;

//必须将PagingBean进行序列化，否则无法通过dubbo进行传输。
public class PagingBean implements java.io.Serializable {

	private static final long serialVersionUID = 7441586107171434690L;

	/**
	 * 缺省的页大小
	 */
	public static final Integer DEFAULT_PAGE_SIZE = 25;

	private Integer pageSize = DEFAULT_PAGE_SIZE.intValue();
	/**
	 * 得到当前页的数据库的第一条记录号
	 * 
	 * @return
	 */
	public Integer start;

	private Integer totalCount = 0;

	/*
	 * 是否显示总页数，当记录数比较大是，查询总记录数是会比较耗时的， 所以有时为了提高查询性能，会把这记录去掉
	 */
	private boolean showTotal = true;

	private int currentPage = 1;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public PagingBean() {
	}

	public PagingBean(int start, int limit) {
		this.pageSize = limit;
		this.start = start;
		this.currentPage = start;
	}

	/*
	 * public PagingBean(Integer currentPage, int limit,int bb) {
	 * this.currentPage = currentPage; this.pageSize = limit; }
	 */

	public boolean isShowTotal() {
		return showTotal;
	}

	public void setShowTotal(boolean showTotal) {
		this.showTotal = showTotal;
	}

	/**
	 * 设置总的记录数
	 * 
	 * @param count
	 */
	public void setTotalCount(int count) {
		this.totalCount = count;
	}

	/**
	 * 每一页显示的条目数
	 * 
	 * @return 每一页显示的条目数
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = Integer.valueOf(pageSize);
	}

	public int getFirstResult() {
		if (this.start != null) {
			return this.start.intValue();
		} else {
			return 0;
		}

	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalPage() {
		int page = this.totalCount / this.pageSize;
		int tmp = this.totalCount % this.pageSize;
		return page + ((tmp == 0) ? 0 : 1);
	}

	public int getFirst() {
		if (pageSize <= 0)
			throw new IllegalArgumentException(
					"[pageSize] must great than zero");
		return (currentPage - 1) * pageSize;

	}

	public int getLast() {
		int last = currentPage * pageSize;
		if (last > totalCount)
			return totalCount;
		return last;
	}

}
