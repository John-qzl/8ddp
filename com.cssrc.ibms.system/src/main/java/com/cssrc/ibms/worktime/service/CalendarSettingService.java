package com.cssrc.ibms.worktime.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.worktime.ICalendarSettingService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.worktime.dao.CalendarSettingDao;
import com.cssrc.ibms.worktime.model.CalendarSetting;
import com.cssrc.ibms.worktime.model.WorkTime;

@Service
public class CalendarSettingService extends BaseService<CalendarSetting> implements ICalendarSettingService{

	@Resource
	private CalendarSettingDao dao;

	protected IEntityDao<CalendarSetting, Long> getEntityDao() {
		return this.dao;
	}

	public List<WorkTime> getByCalendarId(long calendarId, Date startTime) {
		List rtnList = new ArrayList();
		List tmpList = new ArrayList();
		List<CalendarSetting> list = this.dao.getByCalendarId(calendarId,
				startTime);
		for (CalendarSetting calendarSetting : list) {
			String calDay = calendarSetting.getCalDay();
			List<WorkTime> workTimeList = (List<WorkTime>)(List)calendarSetting.getWorkTimeList();
			for (WorkTime workTime : workTimeList) {
				workTime.setCalDay(calDay);
				tmpList.add((WorkTime) workTime.clone());
			}
		}
		int len = tmpList.size();
		for (int i = 0; i < len; i++) {
			WorkTime curTime = (WorkTime) tmpList.get(i);
			if (i < len - 1) {
				int j = i + 1;
				WorkTime nextTime = (WorkTime) tmpList.get(j);
				if (curTime.getEndDateTime().compareTo(
						nextTime.getStartDateTime()) > 0) {
					curTime.setEndDateTime(nextTime.getEndDateTime());
					rtnList.add(curTime);
					i++;
				} else {
					rtnList.add(curTime);
				}
			} else {
				rtnList.add(curTime);
			}
		}
		return rtnList;
	}

	public List<WorkTime> getByCalendarId(long calendarId, Date startTime,
			Date endTime) {
		List rtnList = new ArrayList();
		List tmpList = new ArrayList();

		String startDate = TimeUtil.getDateString(startTime);
		String endDate = TimeUtil.getDateString(endTime);
		List<CalendarSetting> list = this.dao.getSegmentByCalId(
				Long.valueOf(calendarId), startDate, endDate);
		for (CalendarSetting calendarSetting : list) {
			String calDay = calendarSetting.getCalDay();
			List<WorkTime> workTimeList = (List<WorkTime>)(List)calendarSetting.getWorkTimeList();
			for (WorkTime workTime : workTimeList) {
				workTime.setCalDay(calDay);
				tmpList.add((WorkTime) workTime.clone());
			}
		}
		int len = tmpList.size();
		for (int i = 0; i < len; i++) {
			WorkTime curTime = (WorkTime) tmpList.get(i);
			if (i < len - 1) {
				int j = i + 1;
				WorkTime nextTime = (WorkTime) tmpList.get(j);
				if (curTime.getEndDateTime().compareTo(
						nextTime.getStartDateTime()) > 0) {
					curTime.setEndDateTime(nextTime.getEndDateTime());
					rtnList.add(curTime);
					i++;
				} else {
					rtnList.add(curTime);
				}
			} else {
				rtnList.add(curTime);
			}
		}
		return rtnList;
	}

	public List<CalendarSetting> getCalByIdYearMon(Long id, int year, int month) {
		return this.dao.getCalByIdYearMon(id, year, month);
	}

	public void delByCalidYearMon(Long calid, Short year, Short month) {
		this.dao.delByCalidYearMon(calid, year.shortValue(), month.shortValue());
	}

	public void delByCalId(Long[] calIds) {
		this.dao.delByCalId(calIds);
	}
}
