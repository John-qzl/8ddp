package com.cssrc.ibms.core.job.scheduler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 
 * <p>Title:SchedulerService</p>
 * @author Yangbo 
 * @date 2016-8-4下午05:34:31
 */
public class SchedulerService {
	Scheduler scheduler;
	private static HashMap<String, String> mapWeek = new HashMap();
	private static final String schedGroup = "group1";

	public void setScheduler(Scheduler s) {
		this.scheduler = s;
	}

	public boolean addJob(String jobName, String className,
			String parameterJson, String description)
			throws SchedulerException, ClassNotFoundException {
		if (this.scheduler == null)
			return false;
		Class cls = Class.forName(className);
		JobBuilder jb = JobBuilder.newJob(cls);
		jb.withIdentity(jobName, "group1");
		if (StringUtil.isNotEmpty(parameterJson)) {
			setJobMap(parameterJson, jb);
		}
		jb.storeDurably();
		jb.withDescription(description);
		JobDetail jobDetail = jb.build();
		this.scheduler.addJob(jobDetail, true);
		return true;
	}

	public ResultMessage addJob(String jobName, String className,
			Map parameterMap, String description) throws SchedulerException {
		if (this.scheduler == null)
			return new ResultMessage(0, "scheduler 没有配置!");

		ResultMessage resultMsg = null;
		boolean isJobExist = isJobExists(jobName);
		if (isJobExist) {
			resultMsg = new ResultMessage(0, "任务已存在");
			return resultMsg;
		}
		Class cls;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			resultMsg = new ResultMessage(0, "指定的任务类不存在，或者没有实现JOB接口");
			return resultMsg;
		}
		try {
			resultMsg = addJob(jobName, cls, parameterMap, description);
			return resultMsg;
		} catch (Exception e) {
			resultMsg = new ResultMessage(0, e.getMessage());
		}
		return resultMsg;
	}

	public ResultMessage addJob(String jobName, Class cls, Map parameterMap,
			String description) throws SchedulerException,
			ClassNotFoundException {
		if (this.scheduler == null)
			return new ResultMessage(0, "scheduler 没有配置!");

		ResultMessage resultMsg = null;

		JobBuilder jb = JobBuilder.newJob(cls);
		jb.withIdentity(jobName, "group1");
		if (parameterMap != null) {
			JobDataMap map = new JobDataMap();
			map.putAll(parameterMap);
			jb.usingJobData(map);
		}
		jb.storeDurably();
		jb.withDescription(description);
		JobDetail jobDetail = jb.build();
		this.scheduler.addJob(jobDetail, true);
		resultMsg = new ResultMessage(1, "添加任务成功!");
		return resultMsg;
	}

	public boolean isJobExists(String jobName) throws SchedulerException {
		if (this.scheduler == null)
			return false;
		JobKey key = new JobKey(jobName, "group1");
		return this.scheduler.checkExists(key);
	}

	public List<JobDetail> getJobList() throws SchedulerException {
		if (this.scheduler == null) {
			return new ArrayList();
		}
		List<JobDetail> list = new ArrayList();
		GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals("group1");
		Set<JobKey> set = this.scheduler.getJobKeys(matcher);
		for (JobKey jobKey : set) {
			JobDetail detail = this.scheduler.getJobDetail(jobKey);
			list.add(detail);
		}
		return list;
	}

	public List<Trigger> getTriggersByJob(String jobName)
			throws SchedulerException {
		if (this.scheduler == null) {
			return new ArrayList();
		}
		JobKey key = new JobKey(jobName, "group1");
		return (List<Trigger>) scheduler.getTriggersOfJob(key);
	}

	public HashMap<String, Trigger.TriggerState> getTriggerStatus(
			List<Trigger> list) throws SchedulerException {
		if (this.scheduler == null) {
			return new HashMap();
		}
		HashMap map = new HashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Trigger trigger = (Trigger) it.next();
			TriggerKey key = trigger.getKey();
			Trigger.TriggerState state = this.scheduler.getTriggerState(key);
			map.put(key.getName(), state);
		}
		return map;
	}

	public boolean isTriggerExists(String trigName) throws SchedulerException {
		if (this.scheduler == null) {
			return false;
		}
		TriggerKey triggerKey = new TriggerKey(trigName, "group1");
		return this.scheduler.checkExists(triggerKey);
	}

	public void addTrigger(String jobName, String trigName, String planJson)
			throws SchedulerException, ParseException {
		if (this.scheduler == null) {
			return;
		}
		JobKey jobKey = new JobKey(jobName, "group1");
		TriggerBuilder tb = TriggerBuilder.newTrigger();
		tb.withIdentity(trigName, "group1");

		setTrigBuilder(planJson, tb);
		tb.forJob(jobKey);
		Trigger trig = tb.build();
		this.scheduler.scheduleJob(trig);
	}

	public void addTrigger(String jobName, String trigName, int minute)
			throws SchedulerException {
		if (this.scheduler == null) {
			return;
		}
		JobKey jobKey = new JobKey(jobName, "group1");
		TriggerBuilder tb = TriggerBuilder.newTrigger();
		tb.withIdentity(trigName, "group1");
		ScheduleBuilder sb = CalendarIntervalScheduleBuilder
				.calendarIntervalSchedule().withIntervalInMinutes(minute);
		tb.startNow();
		tb.withSchedule(sb);
		tb.withDescription("每:" + minute + "分钟执行!");

		tb.forJob(jobKey);
		Trigger trig = tb.build();
		this.scheduler.scheduleJob(trig);
	}

	private void setTrigBuilder(String planJson, TriggerBuilder<Trigger> tb)
			throws ParseException {
		JSONObject jsonObject = JSONObject.fromObject(planJson);

		PlanObject planObject = (PlanObject) JSONObject.toBean(jsonObject,
				PlanObject.class);
		int type = planObject.getType();
		String value = planObject.getTimeInterval();
		switch (type) {
		case 1:
			Date date = TimeUtil.convertString(value);
			tb.startAt(date);
			tb.withDescription("执行一次,执行时间:" + date.toLocaleString());
			break;
		case 2:
			int minute = Integer.parseInt(value);
			ScheduleBuilder sb = CalendarIntervalScheduleBuilder
					.calendarIntervalSchedule().withIntervalInMinutes(minute);

			tb.startNow();
			tb.withSchedule(sb);
			tb.withDescription("每:" + minute + "分钟执行!");
			break;
		case 3:
			String[] aryTime = value.split(":");
			int hour = Integer.parseInt(aryTime[0]);
			int m = Integer.parseInt(aryTime[1]);
			ScheduleBuilder sb1 = CronScheduleBuilder.dailyAtHourAndMinute(
					hour, m);
			tb.startNow();
			tb.withSchedule(sb1);
			tb.withDescription("每天：" + hour + ":" + m + "执行!");
			break;
		case 4:
			String[] aryExpression = value.split("[|]");
			String week = aryExpression[0];
			String[] aryTime1 = aryExpression[1].split(":");
			String h1 = aryTime1[0];
			String m1 = aryTime1[1];
			String cronExperssion = "0 " + m1 + " " + h1 + " ? * " + week;
			ScheduleBuilder sb4 = CronScheduleBuilder
					.cronSchedule(cronExperssion);
			tb.startNow();
			tb.withSchedule(sb4);
			String weekName = getWeek(week);
			tb.withDescription("每周：" + weekName + "," + h1 + ":" + m1 + "执行!");
			break;
		case 5:
			String[] aryExpression5 = value.split("[|]");
			String day = aryExpression5[0];
			String[] aryTime2 = aryExpression5[1].split(":");
			String h2 = aryTime2[0];
			String m2 = aryTime2[1];
			String cronExperssion1 = "0 " + m2 + " " + h2 + " " + day + " * ?";
			ScheduleBuilder sb5 = CronScheduleBuilder
					.cronSchedule(cronExperssion1);
			tb.startNow();
			tb.withSchedule(sb5);
			String dayName = getDay(day);
			tb.withDescription("每月:" + dayName + "," + h2 + ":" + m2 + "执行!");
			break;
		case 6:
			ScheduleBuilder sb6 = CronScheduleBuilder.cronSchedule(value);
			tb.startNow();
			tb.withSchedule(sb6);
			tb.withDescription("CronTrigger表达式:" + value);
		}
	}

	private String getDay(String day) {
		String[] aryDay = day.split(",");
		int len = aryDay.length;
		String str = "";
		for (int i = 0; i < len; i++) {
			String tmp = aryDay[i];
			tmp = tmp.equals("L") ? "最后一天" : tmp;
			if (i < len - 1) {
				str = str + tmp + ",";
			} else {
				str = str + tmp;
			}
		}
		return str;
	}

	private String getWeek(String week) {
		String[] aryWeek = week.split(",");
		int len = aryWeek.length;
		String str = "";
		for (int i = 0; i < len; i++) {
			if (i < len - 1)
				str = str + (String) mapWeek.get(aryWeek[i]) + ",";
			else
				str = str + (String) mapWeek.get(aryWeek[i]);
		}
		return str;
	}

	private void setJobMap(String json, JobBuilder jb) {
		JSONArray aryJson = JSONArray.fromObject(json);
		ParameterObj[] list = (ParameterObj[]) (ParameterObj[]) JSONArray
				.toArray(aryJson, ParameterObj.class);
		for (int i = 0; i < list.length; i++) {
			ParameterObj obj = list[0];
			String type = obj.getType();
			String name = obj.getName();
			String value = obj.getValue();
			if (type.equals("int")) {
				if (StringUtil.isEmpty(value))
					jb.usingJobData(name, Integer.valueOf(0));
				else {
					jb.usingJobData(name, Integer.valueOf(Integer
							.parseInt(value)));
				}
			} else if (type.equals("long")) {
				if (StringUtil.isEmpty(value))
					jb.usingJobData(name, Integer.valueOf(0));
				else {
					jb.usingJobData(name, Long.valueOf(Long.parseLong(value)));
				}
			} else if (type.equals("float")) {
				if (StringUtil.isEmpty(value))
					jb.usingJobData(name, Double.valueOf(0.0D));
				else {
					jb.usingJobData(name, Float
							.valueOf(Float.parseFloat(value)));
				}
			} else if (type.equals("boolean")) {
				if (StringUtil.isEmpty(value))
					jb.usingJobData(name, Boolean.valueOf(false));
				else {
					jb.usingJobData(name, Boolean.valueOf(Boolean
							.parseBoolean(value)));
				}
			} else
				jb.usingJobData(name, value);
		}
	}

	public void delJob(String jobName) throws SchedulerException {
		if (this.scheduler == null) {
			return;
		}
		JobKey key = new JobKey(jobName, "group1");
		this.scheduler.deleteJob(key);
	}

	public Trigger getTrigger(String triggerName) throws SchedulerException {
		if (this.scheduler == null) {
			return null;
		}
		TriggerKey key = new TriggerKey(triggerName, "group1");
		Trigger trigger = this.scheduler.getTrigger(key);
		return trigger;
	}

	public void delTrigger(String triggerName) throws SchedulerException {
		if (this.scheduler == null) {
			return;
		}
		TriggerKey key = new TriggerKey(triggerName, "group1");
		this.scheduler.unscheduleJob(key);
	}

	public void toggleTriggerRun(String triggerName) throws SchedulerException {
		if (this.scheduler == null) {
			return;
		}
		TriggerKey key = new TriggerKey(triggerName, "group1");
		Trigger.TriggerState state = this.scheduler.getTriggerState(key);
		if (state == Trigger.TriggerState.PAUSED) {
			this.scheduler.resumeTrigger(key);
		} else if (state == Trigger.TriggerState.NORMAL)
			this.scheduler.pauseTrigger(key);
	}

	public void executeJob(String jobName) throws SchedulerException {
		if (this.scheduler == null) {
			return;
		}
		JobKey key = new JobKey(jobName, "group1");
		this.scheduler.triggerJob(key);
	}

	public void start() throws SchedulerException {
		this.scheduler.start();
	}

	public void shutdown() throws SchedulerException {
		this.scheduler.standby();
	}

	public boolean isStarted() throws SchedulerException {
		return this.scheduler.isStarted();
	}

	public boolean isInStandbyMode() throws SchedulerException {
		return this.scheduler.isInStandbyMode();
	}

	static {
		mapWeek.put("MON", "星期一");
		mapWeek.put("TUE", "星期二");
		mapWeek.put("WED", "星期三");
		mapWeek.put("THU", "星期四");
		mapWeek.put("FRI", "星期五");
		mapWeek.put("SAT", "星期六");
		mapWeek.put("SUN", "星期日");
	}
}
