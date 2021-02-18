package com.cssrc.ibms.api.system.model.worktime;

import java.util.Date;

public interface IOverTime {
	void setId(Long xx);
	Long getId();
	void setUserId(Long xx);
	Long getUserId();
	void setStartTime(Date xx);
	Date getStartTime();
	void setEndTime(Date xx);
	Date getEndTime();
	Short getWorkType();
	String getSubject();
	void setSubject(String xx);
	String getMemo();
	void setMemo(String xx);
	void setWorkType(Short xx);
	String getUserName();
	void setUserName(String xx);
	String getsTime();
	void setsTime(String xx);
	String geteTime();
	void seteTime(String xx);
}
