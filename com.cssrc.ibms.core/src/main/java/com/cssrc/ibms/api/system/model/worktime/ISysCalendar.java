package com.cssrc.ibms.api.system.model.worktime;

public interface ISysCalendar {
	void setId(Long xx);
	Long getId();
	void setName(String xx);
	String getName();
	void setMemo(String xx);
	String getMemo();
	void setIsDefault(Short xx);
	Short getIsDefault();
}
