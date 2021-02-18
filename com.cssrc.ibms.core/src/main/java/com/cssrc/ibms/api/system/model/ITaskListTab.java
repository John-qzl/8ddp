package com.cssrc.ibms.api.system.model;

import java.util.Date;

public interface ITaskListTab {

	void setTabName(String tabName);
	
	void setDataNum(String dataNum);
	
	void setTime(Date time);
	
	void setStatus(String status);
	
	void setUrl(String url);
	
	String getDataNum();
}
