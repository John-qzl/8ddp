package com.cssrc.ibms.dp.sync.bean;

import java.util.HashSet;
import java.util.Set;

public class RowBean implements java.io.Serializable{
	private String id;
	private String isfinished;
	private Set cell = new HashSet(0);
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsfinished() {
		return isfinished;
	}
	public void setIsfinished(String isfinished) {
		this.isfinished = isfinished;
	}
	public Set getCell() {
		return cell;
	}
	public void setCell(Set cell) {
		this.cell = cell;
	}
	
}
