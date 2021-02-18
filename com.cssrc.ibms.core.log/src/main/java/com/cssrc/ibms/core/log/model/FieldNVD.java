package com.cssrc.ibms.core.log.model;
/**
 *@author vector
 *@date 2017年9月21日 下午3:41:01
 *@Description Field的name value desc
 * 属性的定义名、值以及描述
 */
public class FieldNVD {
	private String name;
	private Object value;
	private String desc;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
