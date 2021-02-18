package com.cssrc.ibms.api.system.model;

public interface ISerialNumber {
	public Long getId();
	String getAlias();

	void setId(Long genId);

	void setCurDate(String date);

	String getName();
	
	
}