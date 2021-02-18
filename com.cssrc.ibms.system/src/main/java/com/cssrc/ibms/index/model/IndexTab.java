package com.cssrc.ibms.index.model;

import com.cssrc.ibms.core.db.mybatis.page.PageList;


public class IndexTab
{
	protected String title;
	protected String key;
	protected String badge;
	protected boolean active = false;
	protected PageList<?> list;

	public String getTitle()
	{
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBadge() {
		return this.badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public PageList<?> getList() {
		return this.list;
	}

	public void setList(PageList<?> list) {
		this.list = list;
	}
}

