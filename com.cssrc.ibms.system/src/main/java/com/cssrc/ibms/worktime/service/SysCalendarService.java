package com.cssrc.ibms.worktime.service;

import java.util.Iterator;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.worktime.ISysCalendarService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.worktime.dao.CalendarSettingDao;
import com.cssrc.ibms.worktime.dao.SysCalendarDao;
import com.cssrc.ibms.worktime.model.CalendarSetting;
import com.cssrc.ibms.worktime.model.SysCalendar;

@Service
public class SysCalendarService extends BaseService<SysCalendar> implements ISysCalendarService{

	@Resource
	private SysCalendarDao dao;

	@Resource
	private CalendarSettingDao calendarSettingDao;

	protected IEntityDao<SysCalendar, Long> getEntityDao() {
		return this.dao;
	}

	public SysCalendar getDefaultCalendar() {
		return this.dao.getDefaultCalendar();
	}

	public void setDefaultCal(Long id) {
		this.dao.setNotDefaultCal();
		SysCalendar syscal = (SysCalendar) getById(id);
		syscal.setIsDefault(new Short("1"));
		update(syscal);
	}

	public void saveCalendar(String json) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(json);
		Long id = Long.valueOf(jsonObject.getLong("id"));
		String name = jsonObject.getString("name");
		String memo = jsonObject.getString("memo");
		short year = (short) jsonObject.getInt("year");
		short month = (short) jsonObject.getInt("month");
		JSONArray aryDay = jsonObject.getJSONArray("days");
		if (id.longValue() == 0L) {
			id = Long.valueOf(UniqueIdUtil.genId());
			SysCalendar defaultCal = this.dao.getDefaultCalendar();
			SysCalendar sysCalendar = new SysCalendar();
			sysCalendar.setId(id);
			sysCalendar.setName(name);
			sysCalendar.setMemo(memo);
			if (defaultCal == null) {
				sysCalendar.setIsDefault(Short.valueOf((short) 1));
			} else {
				sysCalendar.setIsDefault(Short.valueOf((short) 0));
			}
			add(sysCalendar);
		} else {
			SysCalendar sysCalendar = (SysCalendar) this.dao.getById(id);
			sysCalendar.setName(name);
			sysCalendar.setMemo(memo);
			this.dao.update(sysCalendar);
			this.calendarSettingDao.delByCalidYearMon(id, year, month);
		}
		addCalendarSetting(id, year, month, aryDay);
	}

	private void addCalendarSetting(Long calendarId, short year, short month,
			JSONArray aryDay) throws Exception {
		for (Iterator localIterator = aryDay.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject jsonObj = (JSONObject) obj;

			short day = (short) jsonObj.getInt("day");
			short type = (short) jsonObj.getInt("type");
			Long worktimeid = Long.valueOf(0L);
			if (type == 1) {
				worktimeid = Long.valueOf(jsonObj.getLong("worktimeid"));
			}
			CalendarSetting setting = new CalendarSetting();
			setting.setId(Long.valueOf(UniqueIdUtil.genId()));
			setting.setCalendarId(calendarId);
			setting.setYears(Short.valueOf((short) year));
			setting.setMonths(Short.valueOf((short) month));
			setting.setDays(Short.valueOf((short) day));
			setting.setType(Short.valueOf((short) type));
			setting.setWorkTimeId(worktimeid);
			this.calendarSettingDao.add(setting);
		}
	}
}
