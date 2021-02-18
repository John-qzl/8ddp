package com.cssrc.ibms.api.system.model.worktime;

public interface ICalendarAssign {
	void setCanlendarId(Long Id);
	Long getCanlendarId();
	void setAssignType(Short type);
	Short getAssignType();
	void setAssignId(Long assignId);
	Long getAssignId();
	String getCalendarName();
	void setCalendarName(String name);
	String getAssignUserName();
	void setAssignUserName(String username);
}
