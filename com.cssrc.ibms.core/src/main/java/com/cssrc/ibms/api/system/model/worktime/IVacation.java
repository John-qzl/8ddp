package com.cssrc.ibms.api.system.model.worktime;

import java.util.Date;

public interface IVacation {
	void setId(Long xx);
	Long getId();
	void setName(String xx);
	String getName();
	void setYears(Short xx);
	Short getYears();
	void setStatTime(Date xx);
	Date getStatTime();
	void setEndTime(Date xx);
	Date getEndTime();
	String getsTime();
	void setsTime(String xx);
	String geteTime();
	void seteTime(String xx);
}
