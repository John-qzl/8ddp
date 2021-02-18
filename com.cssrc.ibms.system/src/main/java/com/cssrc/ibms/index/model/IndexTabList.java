package com.cssrc.ibms.index.model;

import java.util.List;

import com.cssrc.ibms.core.db.mybatis.page.PagingBean;


public class IndexTabList
{
	protected String curTab;
	private PagingBean pageBean = new PagingBean();
	protected List<IndexTab> indexTabList;

	public List<IndexTab> getIndexTabList()
	{
		return this.indexTabList;
	}

	public void setIndexTabList(List<IndexTab> indexTabList) {
		this.indexTabList = indexTabList;
	}

	public String getCurTab() {
		return this.curTab;
	}

	public void setCurTab(String curTab) {
		this.curTab = curTab;
	}

	public PagingBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PagingBean pageBean) {
		this.pageBean = pageBean;
	}


}

