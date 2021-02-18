package com.cssrc.ibms.api.activity.model;

public interface ITaskAmount {

	int getTotal();

	int getNotRead();

	Long getTypeId();
	
	public Long getDefId();

    void setDefId(Long defId);
}