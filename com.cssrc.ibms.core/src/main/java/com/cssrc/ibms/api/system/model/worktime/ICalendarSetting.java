package com.cssrc.ibms.api.system.model.worktime;

import java.util.List;

public interface ICalendarSetting {
	void setId(Long id);
	Long getId();
	void setCalendarId(Long id);
	Long getCalendarId();
	void setYears(Short y);
	Short getYears();
	void setMonths(Short m);
	Short getMonths();
	void setDays(Short d);
	Short getDays();
	void setType(Short type);
	Short getType();
	void setWorkTimeId(Long timeId);
	Long getWorkTimeId();
	void setCalDay(String day);
	String getCalDay();
	String getWtName();
	void setWtName(String name);
	boolean getIsLegal();
	void setIsLegal(boolean isleagle);
	List<IWorkTime> getWorkTimeList();
	void setWorkTimeList(List<IWorkTime> list);
}
