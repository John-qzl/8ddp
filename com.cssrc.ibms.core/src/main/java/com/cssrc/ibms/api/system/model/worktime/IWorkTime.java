package com.cssrc.ibms.api.system.model.worktime;

import java.util.Date;

public interface IWorkTime {
	void setId(Long xx);
	Long getId(); 
	void setSettingId(Long xx);
	Long getSettingId();
	void setStartTime(String xx);
	String getStartTime();
	void setEndTime(String xx);
	String getEndTime();
	String getMemo();
	void setMemo(String xx);
	Date getStartDateTime();
	void setStartDateTime(Date xx);
	Date getEndDateTime();
	void setEndDateTime(Date xx);
	String getCalDay();
	void setCalDay(String xx);
}
