package com.cssrc.ibms.worktime.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IHistoryProcessInstanceService;
import com.cssrc.ibms.api.activity.intf.IHistoryTaskInstanceService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.worktime.ICalendarAssignService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.worktime.dao.CalendarAssignDao;
import com.cssrc.ibms.worktime.dao.CalendarSettingDao;
import com.cssrc.ibms.worktime.dao.SysCalendarDao;
import com.cssrc.ibms.worktime.model.CalendarAssign;
import com.cssrc.ibms.worktime.model.CalendarSetting;
import com.cssrc.ibms.worktime.model.OverTime;
import com.cssrc.ibms.worktime.model.SysCalendar;
import com.cssrc.ibms.worktime.model.WorkTime;

@Service
public class CalendarAssignService extends BaseService<CalendarAssign> implements ICalendarAssignService{

	@Resource
	private CalendarAssignDao dao;

	@Resource
	private ISysOrgService sysOrgSevice;

	@Resource
	private SysCalendarDao sysCalendarDao;

	@Resource
	private CalendarSettingService calendarSettingService;

	@Resource
	private CalendarSettingDao calendarSettingDao;

	@Resource
	private OverTimeService overTimeService;

	@Resource
	private IHistoryProcessInstanceService historyProcessInstanceService;

	@Resource
	private IHistoryTaskInstanceService historyTaskInstanceService;

	protected IEntityDao<CalendarAssign, Long> getEntityDao() {
		return this.dao;
	}

	public Long getCalendarIdByUserId(Long userId) {
	    //首先查找用户分配的工作日历
		CalendarAssign calendarAssign = this.dao.getByAssignId(
				CalendarAssign.User, userId.longValue());
		if (calendarAssign == null) {
		    //如果日历为空，通过组织查找日历。这块代码的修改，应该有不同策略来查找日历
			ISysOrg sysOrg = this.sysOrgSevice.getPrimaryOrgByUserId(userId);
			if (sysOrg != null) {
				long orgId = sysOrg.getOrgId().longValue();
				calendarAssign = this.dao.getByAssignId(
						CalendarAssign.Organization, orgId);
			}
		}
		//如果不为空，直接返回用户日历
		if (calendarAssign != null) {
			return calendarAssign.getCanlendarId();
		}
		//如果没有找到用户对应的工作日历，查找全局默认的工作日历
		SysCalendar sysCalendar = this.sysCalendarDao.getDefaultCalendar();
		if (sysCalendar != null) {
			return sysCalendar.getId();
		}

		return Long.valueOf(0L);
	}

	private List<WorkTime> getBycalList(List<CalendarSetting> list) {
		List tmplist = new ArrayList();
		List worklist = new ArrayList();
		for (CalendarSetting setting : list) {
			String calDay = setting.getCalDay();
			List<WorkTime> workTimeList = (List<WorkTime>)(List)setting.getWorkTimeList();
			for (WorkTime work : workTimeList) {
				work.setCalDay(calDay);
				tmplist.add((WorkTime) work.clone());
			}
		}
		int len = tmplist.size();
		for (int i = 0; i < len; i++) {
			WorkTime workTime = (WorkTime) tmplist.get(i);

			if (i < len - 1) {
				int j = i + 1;
				WorkTime nextTime = (WorkTime) tmplist.get(j);

				if (workTime.getEndDateTime().compareTo(
						nextTime.getStartDateTime()) > 0) {
					workTime.setEndDateTime(nextTime.getEndDateTime());
					worklist.add(workTime);
					i++;
				} else {
					worklist.add(workTime);
				}
			} else {
				worklist.add(workTime);
			}
		}
		return worklist;
	}

	@Deprecated
	public String getTaskTime(Date startDate, Date endDate, long userId) {
		long workTime = getRealWorkTime(startDate, endDate,
				Long.valueOf(userId)).longValue();
		return TimeUtil.getTime(Long.valueOf(workTime));
	}

	public Long getRealWorkTime(Date startTime, Date endTime, Long userId) {
		if (userId == null) {
			userId = Long.valueOf(0L);
		}

		long taskTime = getTaskMillsTime(startTime, endTime, userId.longValue());

		long overTime = getOverTime(userId.longValue(), startTime, endTime);
		long leaveTime = getLeaveTime(userId.longValue(), startTime, endTime);

		if (taskTime + overTime <= leaveTime) {
			return Long.valueOf(endTime.getTime() - startTime.getTime());
		}
		long workTime = taskTime + overTime - leaveTime;
		return Long.valueOf(workTime);
	}

	public long getTaskMillsTime(Date startDate, Date endDate, long userId) {
		String start = TimeUtil.getDateString(startDate);
		String end = TimeUtil.getDateString(endDate);

		Long calendarId = getCalendarIdByUserId(Long.valueOf(userId));
		if (calendarId.longValue() == 0L) {
			return endDate.getTime() - startDate.getTime();
		}

		List list = this.calendarSettingDao.getSegmentByCalId(calendarId,
				start, end);
		if (BeanUtils.isEmpty(list)) {
			return endDate.getTime() - startDate.getTime();
		}

		List worklist = getBycalList(list);

		long taskTime = 0L;
		int leng = worklist.size();
		for (int i = 0; i < leng; i++) {
			WorkTime workTime = (WorkTime) worklist.get(i);

			Date startWorkTime = workTime.getStartDateTime();

			Date endWorkTime = workTime.getEndDateTime();

			if (startDate.compareTo(startWorkTime) < 0) {
				if ((endDate.compareTo(startWorkTime) >= 0)
						&& (endDate.compareTo(endWorkTime) <= 0)) {
					taskTime += endDate.getTime() - startWorkTime.getTime();
				} else if (endDate.compareTo(endWorkTime) > 0)
					taskTime += endWorkTime.getTime() - startWorkTime.getTime();
			} else {
				if ((startDate.compareTo(startWorkTime) < 0)
						|| (startDate.compareTo(endWorkTime) > 0))
					continue;
				if (endDate.compareTo(endWorkTime) <= 0) {
					taskTime += endDate.getTime() - startDate.getTime();
				} else {
					taskTime += endWorkTime.getTime() - startDate.getTime();
				}
			}
		}
		return taskTime;
	}

	public long getOverTime(long userId, Date startTime, Date endTime) {
		long overTime = 0L;
		List<OverTime> listOverTime = this.overTimeService.getListByUserId(
				userId, 1, startTime, endTime);
		if (listOverTime != null) {
			for (OverTime workTime : listOverTime) {
				Date start = workTime.getStartTime();
				Date end = workTime.getEndTime();

				if (startTime.compareTo(start) < 0) {
					if ((endTime.compareTo(start) >= 0)
							&& (endTime.compareTo(end) <= 0)) {
						overTime += endTime.getTime() - start.getTime();
					} else if (endTime.compareTo(end) > 0)
						overTime += end.getTime() - start.getTime();
				} else {
					if ((startTime.compareTo(start) < 0)
							|| (startTime.compareTo(end) > 0))
						continue;
					if (endTime.compareTo(end) <= 0) {
						overTime += endTime.getTime() - startTime.getTime();
					} else {
						overTime += end.getTime() - start.getTime();
					}
				}
			}
		}

		return overTime;
	}

	public long getLeaveTime(long userId, Date startTime, Date endTime) {
		long leaveTime = 0L;
		List listLeaveTime = this.overTimeService.getListByUserId(userId, 2,
				startTime, endTime);
		if (listLeaveTime != null) {
			List<OverTime> realLeaveTime = getRealLeaveList(listLeaveTime,
					startTime, endTime);
			for (OverTime leave : realLeaveTime) {
				Date start = leave.getStartTime();
				Date end = leave.getEndTime();

				if (startTime.compareTo(start) < 0) {
					if ((endTime.compareTo(start) >= 0)
							&& (endTime.compareTo(end) <= 0)) {
						leaveTime += endTime.getTime() - start.getTime();
					} else if (endTime.compareTo(end) > 0)
						leaveTime += end.getTime() - start.getTime();
				} else {
					if ((startTime.compareTo(start) < 0)
							|| (startTime.compareTo(end) > 0))
						continue;
					if (endTime.compareTo(end) <= 0) {
						leaveTime += endTime.getTime() - startTime.getTime();
					} else {
						leaveTime += end.getTime() - startTime.getTime();
					}
				}
			}
		}
		return leaveTime;
	}

	public List<OverTime> getRealLeaveList(List<OverTime> leaveList,
			Date startTime, Date endTime) {
		List realList = new ArrayList();
		for (OverTime leave : leaveList) {
			Date start = leave.getStartTime();
			Date end = leave.getEndTime();
			long userId = leave.getUserId().longValue();
			Long calendarId = getCalendarIdByUserId(Long.valueOf(userId));
			if (calendarId.longValue() == 0L) {
				return leaveList;
			}

			List list = this.calendarSettingDao.getSegmentByCalId(calendarId,
					TimeUtil.getDateString(start), TimeUtil.getDateString(end));

			if (BeanUtils.isEmpty(list)) {
				return leaveList;
			}

			List<WorkTime> workList = getBycalList(list);
			if (BeanUtils.isEmpty(workList)) {
				return leaveList;
			}

			for (WorkTime workTime : workList) {
				leave = new OverTime();
				Date startWork = workTime.getStartDateTime();
				Date endWork = workTime.getEndDateTime();

				if (start.compareTo(startWork) < 0) {
					if ((end.compareTo(startWork) >= 0)
							&& (end.compareTo(endWork) <= 0)) {
						leave.setStartTime(startWork);
						leave.setEndTime(end);
					} else if (end.compareTo(endWork) > 0) {
						leave.setStartTime(startWork);
						leave.setEndTime(endWork);
					}

				} else if ((start.compareTo(startWork) >= 0)
						&& (start.compareTo(endWork) <= 0)) {
					if (end.compareTo(endWork) <= 0) {
						leave.setStartTime(start);
						leave.setEndTime(end);
					} else {
						leave.setStartTime(start);
						leave.setEndTime(endWork);
					}
				}
				if ((leave.getEndTime() != null)
						&& (leave.getStartTime() != null)) {
					realList.add(leave);
				}
			}
		}

		return realList;
	}

	public List<WorkTime> getRealWorkList(long calendarId, long userId,
			Date startTime) {
		List<OverTime> overTimeList = this.overTimeService.getListByStart(
				startTime, userId, 1);
		List<OverTime> leaveTimeList = this.overTimeService.getListByStart(
				startTime, userId, 2);
		List<WorkTime> worklist = this.calendarSettingService.getByCalendarId(
				calendarId, startTime);

		List realWorklist = new ArrayList();
		if (leaveTimeList.size() == 0)
			realWorklist.addAll(worklist);
		else {
			for (OverTime leave : leaveTimeList) {
				Date start = leave.getStartTime();
				Date end = leave.getEndTime();
				for (WorkTime workTime : worklist) {
					Date startWork = workTime.getStartDateTime();
					Date endWork = workTime.getEndDateTime();

					if (start.compareTo(startWork) <= 0) {
						if ((end.compareTo(startWork) >= 0)
								&& (end.compareTo(endWork) <= 0)) {
							workTime.setStartDateTime(end);
							workTime.setEndDateTime(endWork);
							realWorklist.add(workTime);
						} else if (end.compareTo(endWork) <= 0) {
							realWorklist.add(workTime);
						} else {
							this.logger.info(workTime.getCalDay() + ",请假了。。。");
						}

					} else if ((start.compareTo(startWork) > 0)
							&& (start.compareTo(endWork) <= 0)) {
						if (end.compareTo(endWork) <= 0) {
							workTime.setStartDateTime(startWork);
							workTime.setEndDateTime(start);
							realWorklist.add(workTime);
							WorkTime work = new WorkTime();
							work.setStartDateTime(end);
							work.setEndDateTime(endWork);
							realWorklist.add(work);
						} else {
							workTime.setStartDateTime(startWork);
							workTime.setEndDateTime(start);
							realWorklist.add(workTime);
						}
					} else
						realWorklist.add(workTime);
				}
			}
		}

		if (overTimeList.size() > 0) {
			for (OverTime oTime : overTimeList) {
				Date start = oTime.getStartTime();
				Date end = oTime.getEndTime();
				WorkTime work = new WorkTime();
				work.setStartDateTime(start);
				realWorklist.add(work);
			}
		}
		return realWorklist;
	}

	public List<WorkTime> getTaskTimeByDefault(Date startDate) {
		SysCalendar sysCalendar = this.sysCalendarDao.getDefaultCalendar();
		if (sysCalendar != null) {
			Long calendarId = sysCalendar.getId();
			List worklist = this.calendarSettingService.getByCalendarId(
					calendarId.longValue(), startDate);
			return worklist;
		}
		return null;
	}

	public Date getRelativeStartTime(String actInstanceId, String nodeId,
			Integer eventType) {
		HistoricTaskInstanceEntity historicTaskInstanceEntity = this.historyTaskInstanceService
				.getByInstanceIdAndNodeId(actInstanceId, nodeId);
		if (historicTaskInstanceEntity != null) {
			if (new Integer(0).equals(eventType)) {
				return historicTaskInstanceEntity.getStartTime();
			}
			return historicTaskInstanceEntity.getEndTime();
		}
		HistoricProcessInstanceEntity historicProcessInstanceEntity = this.historyProcessInstanceService
				.getByInstanceIdAndNodeId(actInstanceId, nodeId);
		if (historicProcessInstanceEntity != null)
			return historicProcessInstanceEntity.getStartTime();
		return null;
	}

	public Date getTaskTimeByUser(Date startDate, int minutes, long userId) {
		int remainMinute = 0;
		List<WorkTime> list = null;

		if (userId == 0L) {
			list = getTaskTimeByDefault(startDate);
		} else {
			Long calendarId = getCalendarIdByUserId(Long.valueOf(userId));

			if (calendarId.longValue() == 0L) {
				return new Date(TimeUtil.getNextTime(1, minutes,
						startDate.getTime()));
			}

			list = getRealWorkList(calendarId.longValue(), userId, startDate);
		}
		if (list == null)
			return new Date(TimeUtil.getNextTime(1, minutes,
					startDate.getTime()));
		boolean isBegin = false;
		Date endTime = null;
		for (WorkTime workTime : list) {
			Date startTime = workTime.getStartDateTime();
			endTime = workTime.getEndDateTime();

			if ((startDate.compareTo(startTime) >= 0)
					&& (endTime.compareTo(startDate) > 0)) {
				isBegin = true;
				int seconds = TimeUtil.getSecondDiff(endTime, startDate);
				int minute = seconds / 60;
				remainMinute = minutes - minute;
				if (remainMinute <= 0)
					return new Date(TimeUtil.getNextTime(1, minutes,
							startDate.getTime()));
			} else {
				if ((startDate.compareTo(startTime) < 0) && (remainMinute == 0)) {
					isBegin = true;
					int seconds = TimeUtil.getSecondDiff(endTime, startTime);
					int minute = seconds / 60;
					remainMinute = minutes - minute;
					if (remainMinute <= 0) {
						return new Date(TimeUtil.getNextTime(1, minutes,
								startTime.getTime()));
					}
				}

				if (isBegin) {
					Date tmpDate = new Date(TimeUtil.getNextTime(1,
							remainMinute, startTime.getTime()));
					if (tmpDate.compareTo(endTime) > 0) {
						int seconds = TimeUtil
								.getSecondDiff(endTime, startTime);
						int minute = seconds / 60;
						remainMinute -= minute;
					} else {
						return new Date(TimeUtil.getNextTime(1, remainMinute,
								startTime.getTime()));
					}
				}
			}
		}
		if ((remainMinute > 0) && (endTime != null)) {
			return new Date(TimeUtil.getNextTime(1, remainMinute,
					endTime.getTime()));
		}
		return new Date(TimeUtil.getNextTime(1, minutes, startDate.getTime()));
	}

	public List<Map> getAssignUserType() {
		List list = new ArrayList();

		Map map = new HashMap();
		map.put("id", "1");
		map.put("name", "用户");
		list.add(map);

		map = new HashMap();
		map.put("id", "2");
		map.put("name", "组织");
		list.add(map);

		return list;
	}

	public void delByCalId(Long[] calIds) {
		this.dao.delByCalId(calIds);
	}

	public CalendarAssign getbyAssignId(Long assignId) {
		return this.dao.getbyAssignId(assignId);
	}
}
