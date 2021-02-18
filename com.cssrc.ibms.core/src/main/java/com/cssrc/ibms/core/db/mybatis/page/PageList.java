package com.cssrc.ibms.core.db.mybatis.page;

import java.util.ArrayList;


public class PageList<E> extends ArrayList<E>
{
	/*private PageBean pageBean = new PageBean();*/
	private PagingBean pageBean = new PagingBean();

	/*public PageBean getPageBean()
	{
		return this.pageBean;
	}

	public void setPageBean(PageBean pageBean)
	{
		this.pageBean = pageBean;
	}*/

	public int getTotalPage()
	{
	/*	return this.pageBean.getTotalPage();*/
		return this.pageBean.getTotalPage();
	}

	public int getTotalCount() {
		/*return this.pageBean.getTotalCount();*/
		return this.pageBean.getTotalCount();
	}

	
	
	public PagingBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PagingBean pageBean) {
		this.pageBean = pageBean;
	}
	
	
}

