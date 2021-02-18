package com.cssrc.ibms.core.user.model.custom;

public class ListConf {
	private String name ="";
	private int dataNum = 10;
	private Long displayId;
	private String advancedQueryKey;
	private String displayField;
	private String sortField;
	private String html = "";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDataNum() {
		return dataNum;
	}
	public void setDataNum(int dataNum) {
		this.dataNum = dataNum;
	}
	public Long getDisplayId() {
		return displayId;
	}
	public void setDisplayId(Long displayId) {
		this.displayId = displayId;
	}
	public String getAdvancedQueryKey() {
		return advancedQueryKey;
	}
	public void setAdvancedQueryKey(String advancedQueryKey) {
		this.advancedQueryKey = advancedQueryKey;
	}
	public String getDisplayField() {
		return displayField;
	}
	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	
}
