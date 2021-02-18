package com.cssrc.ibms.worktime.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.worktime.model.CalendarSetting;

@Repository
public class CalendarSettingDao extends BaseDao<CalendarSetting> {
	public Class getEntityClass() {
		return CalendarSetting.class;
	}

	public List<CalendarSetting> getByCalendarId(long calendarId, Date startTime) {
		String start = TimeUtil.getDateString(startTime);
		Map map = new HashMap();
		map.put("startTime", start);
		map.put("calendarId", Long.valueOf(calendarId));
		List list = getBySqlKey("getByCalendarId", map);
		return list;
	}

	public List<CalendarSetting> getSegmentByCalId(Long calendarId,
			String startDate, String endDate) {
		String curDate = TimeUtil.getCurrentDate();
		Map map = new HashMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("calendarId", calendarId);
		List list = getBySqlKey("getSegmentByCalId", map);
		return list;
	}

	public List<CalendarSetting> getCalByIdYearMon(Long id, int year, int month) {
		Map map = new HashMap();
		map.put("id", id);
		map.put("year", Integer.valueOf(year));
		map.put("month", Integer.valueOf(month));
		List list = getBySqlKey("getCalByIdYearMon", map);
		return list;
	}

	public void delByCalidYearMon(Long calid, short year, short month) {
		Map map = new HashMap();
		map.put("id", calid);
		map.put("year", Short.valueOf((short) year));
		map.put("month", Short.valueOf((short) month));
		delBySqlKey("delByCalidYearMon", map);
	}

	public void delByCalId(Long[] calIds) {
		for (Long calId : calIds)
			delBySqlKey("delByCalId", calId);
	}
}
