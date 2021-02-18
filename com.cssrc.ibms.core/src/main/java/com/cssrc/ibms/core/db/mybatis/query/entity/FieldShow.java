package com.cssrc.ibms.core.db.mybatis.query.entity;
/**
 * 
 * <p>Title:FieldShow</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:17:31
 */
public class FieldShow {
	protected String name;
	protected String desc;
	protected int show = 0;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getShow() {
		return this.show;
	}

	public void setShow(int show) {
		this.show = show;
	}
}
