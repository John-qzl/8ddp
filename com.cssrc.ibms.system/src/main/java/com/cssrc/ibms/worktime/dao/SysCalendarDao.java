package com.cssrc.ibms.worktime.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.worktime.model.SysCalendar;

@Repository
public class SysCalendarDao extends BaseDao<SysCalendar> {
	public Class getEntityClass() {
		return SysCalendar.class;
	}

	public SysCalendar getDefaultCalendar() {
		return (SysCalendar) getUnique("getDefaultCalendar", null);
	}

	public void setNotDefaultCal() {
		update("setNotDefaultCal", null);
	}
}
