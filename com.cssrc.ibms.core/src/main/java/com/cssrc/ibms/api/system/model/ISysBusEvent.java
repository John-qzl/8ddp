package com.cssrc.ibms.api.system.model;

public interface ISysBusEvent {

	Long getId();

	void setFormkey(String string);

	String getPreScript();

	String getAfterScript();
	
}