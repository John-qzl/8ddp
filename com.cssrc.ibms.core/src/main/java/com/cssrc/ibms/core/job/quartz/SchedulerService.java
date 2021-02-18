package com.cssrc.ibms.core.job.quartz;

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
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.matchers.GroupMatcher;

import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 定时框架。
 * <pre>
 * 实现功能：
 * 1.支持添加，删除定时任务。
 * 2.支持对任务添加或删除计划。 
 * 
 * </pre>
 * @author zhulongchao
 *
 */
public class SchedulerService {
	

	Scheduler scheduler;
	
	public void setScheduler(Scheduler s){
		this.scheduler=s;
	}
	
	
	
	
	private static HashMap<String, String> mapWeek;
	
	static{
		//"MON,TUE,WED,THU,FRI,SAT,SUN"
		mapWeek=new HashMap<String, String>();
		mapWeek.put("MON", "星期一");
		mapWeek.put("TUE", "星期二");
		mapWeek.put("WED", "星期三");
		mapWeek.put("THU", "星期四");
		mapWeek.put("FRI", "星期五");
		mapWeek.put("SAT", "星期六");
		mapWeek.put("SUN", "星期日");
		 
	}
	
	private static final String schedGroup="ibms";
	
	

	/**
	 * 添加任务
	 * @param jobName 任务名称
	 * @param className 类名称
	 * @param parameterJson 参数
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public boolean addJob(String jobName,String className,String parameterJson,String description) throws SchedulerException, ClassNotFoundException{
		if(scheduler==null ) return false;
		try{
			Class<Job> cls  = (Class<Job>)Class.forName(className);
			JobBuilder jb =JobBuilder.newJob(cls);
			jb.withIdentity(jobName, schedGroup);
			if(StringUtil.isNotEmpty(parameterJson)){
				setJobMap(parameterJson,jb);
			}
			jb.storeDurably();
			jb.withDescription(description);
			JobDetail jobDetail= jb.build();
			scheduler.addJob(jobDetail, true);
			return true;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		
	}
	

	
	
	/**
	 * 添加任务
	 * @param jobName 任务名称
	 * @param className 类的全路径
	 * @param parameterMap 参数
	 * @param description 任务描述
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public ResultMessage  addJob(String jobName,String className,Map parameterMap,String description) throws SchedulerException{
		if(scheduler==null ) return new ResultMessage(ResultMessage.Fail,ContextUtil.getMessages("service.scheduler.schedulerNoSet")); ;
		
		ResultMessage resultMsg=null;
		boolean isJobExist=isJobExists(jobName);
		if(isJobExist){
			resultMsg=new ResultMessage(ResultMessage.Fail,ContextUtil.getMessages("service.scheduler.taskIsExist"));
			return resultMsg;
		}
		Class<Job> cls;
		try{
			cls  = (Class<Job>)Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			resultMsg=new ResultMessage(ResultMessage.Fail,ContextUtil.getMessages("service.scheduler.taskClassIsNotExist"));
			return resultMsg;
		}
		
		try{
			resultMsg =addJob(jobName,cls,parameterMap,description);
			return resultMsg;
		}
		catch (Exception e) {
			resultMsg=new ResultMessage(ResultMessage.Fail,e.getMessage());
			return resultMsg;
		}
	}
	
	/**
	 * 
	 * @param jobName
	 * @param cls
	 * @param parameterMap
	 * @param description
	 * @return
	 */
	public ResultMessage addJob(String jobName,Class cls,Map parameterMap,String description)  {
		if(scheduler==null ) return new ResultMessage(ResultMessage.Fail,ContextUtil.getMessages("service.scheduler.schedulerNoSet")); 
		
		ResultMessage resultMsg=null;
		try{
			JobBuilder jb =JobBuilder.newJob(cls);
			jb.withIdentity(jobName, schedGroup);
			if(parameterMap!=null){
				JobDataMap map=new JobDataMap();
				map.putAll(parameterMap);
				jb.usingJobData(map);
			}
			jb.storeDurably();
			jb.withDescription(description);
			JobDetail jobDetail= jb.build();
			scheduler.addJob(jobDetail, true);
			resultMsg=new ResultMessage(ResultMessage.Success,ContextUtil.getMessages("controller.add.success"));
			return resultMsg;
		}
		catch (Exception e) {
			resultMsg=new ResultMessage(ResultMessage.Fail,e.getMessage());
			return resultMsg;
		}
	}
	
	
	/**
	 * 取得任务是否存在
	 * @param jobName
	 * @return
	 * @throws SchedulerException
	 */
	public boolean isJobExists(String jobName) throws SchedulerException{
		if(scheduler==null ) return false ;
		JobKey key=new JobKey(jobName, schedGroup);
		return scheduler.checkExists(key);
	}
	/**
	 * 获取job
	 * */
	public JobDetail getJob(String jobName) throws SchedulerException{
		if(scheduler==null ) return null ;
		JobKey key=new JobKey(jobName, schedGroup);
		return scheduler.getJobDetail(key);
	}
	
	/**
	 * 取得任务列表
	 * @return
	 * @throws SchedulerException
	 */
    public List<JobDetail> getJobList()
        throws SchedulerException
    {
        if (scheduler == null)
        {
            return new ArrayList<JobDetail>();
        }
        List<JobDetail> list = new ArrayList<JobDetail>();
        GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(schedGroup);
        Set<JobKey> set = scheduler.getJobKeys(matcher);
        for (JobKey jobKey : set)
        {
            try
            {
                JobDetail detail = scheduler.getJobDetail(jobKey);
                list.add(detail);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
        }
        return list;
    }
	
	/**
	 * 根据任务名称获取触发器
	 * @param jobName
	 * @return
	 * @throws SchedulerException
	 */
	public List<Trigger> getTriggersByJob(String jobName) throws SchedulerException  {
		if(scheduler==null ){
			return new ArrayList<Trigger>();
		}
		JobKey key=new JobKey(jobName, schedGroup);
		return (List<Trigger>) scheduler.getTriggersOfJob(key);
	}
	
	/**
	 * 取得触发器的状态
	 * @param list
	 * @return
	 * @throws SchedulerException
	 */
	public HashMap<String, Trigger.TriggerState> getTriggerStatus( List<Trigger> list) throws SchedulerException{
		if(scheduler==null ){
			return new HashMap<String, Trigger.TriggerState>();
		}
		HashMap<String, Trigger.TriggerState> map=new HashMap<String, Trigger.TriggerState>();
		for(Iterator<Trigger> it=list.iterator();it.hasNext();){
			Trigger trigger=it.next();
			TriggerKey key=trigger.getKey();
			Trigger.TriggerState state= scheduler.getTriggerState(key);
			map.put(key.getName(), state);
		}
		return map;
	}
	
	/**
	 * 判断计划是否存在
	 * @param trigName 触发器名称
	 * @return
	 * @throws SchedulerException
	 */
	public boolean isTriggerExists(String trigName) throws SchedulerException{
		if(scheduler==null ){
			return false;
		}
		TriggerKey triggerKey=new TriggerKey(trigName, schedGroup);
		return scheduler.checkExists(triggerKey);
	}
	
	/**
	 * 添加计划
	 * @param jobName 任务名
	 * @param trigName 计划名
	 * @param planJson 
	 * @param description
	 * @throws SchedulerException
	 * @throws ParseException 
	 */
	public void addTrigger(String jobName,String trigName, String planJson) throws SchedulerException, ParseException{
		if(scheduler==null ){
			return ;
		}
		JobKey jobKey=new JobKey(jobName, schedGroup);
		TriggerBuilder<Trigger> tb=  TriggerBuilder.newTrigger();
		tb.withIdentity(trigName,schedGroup);
		//tb.withDescription(description);
		setTrigBuilder(planJson,tb);
		tb.forJob(jobKey);
		Trigger trig=tb.build();
		scheduler.scheduleJob(trig);
	}
	
	/**
	 * 添加触发器
	 * @param jobName
	 * @param trigName
	 * @param minute
	 * @throws SchedulerException
	 */
	public void addTrigger(String jobName,String trigName, int minute) throws SchedulerException{
		if(scheduler==null ){
			return ;
		}
		JobKey jobKey=new JobKey(jobName, schedGroup);
		TriggerBuilder<Trigger> tb=  TriggerBuilder.newTrigger();
		tb.withIdentity(trigName,schedGroup);
		ScheduleBuilder sb= CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMinutes(minute);
		tb.startNow();
		tb.withSchedule(sb);
		tb.withDescription(ContextUtil.getMessages("service.scheduler.minute",new Object[]{minute}));
		
		tb.forJob(jobKey);
		Trigger trig=tb.build();
		scheduler.scheduleJob(trig);
	}
	
	private void setTrigBuilder(String planJson,TriggerBuilder<Trigger> tb) throws ParseException{
		JSONObject jsonObject= JSONObject.fromObject(planJson);
		
		PlanObject planObject=(PlanObject)JSONObject.toBean(jsonObject, PlanObject.class) ;
		int type=planObject.getType();
		String value=planObject.getTimeInterval();
		switch(type){
			//启动一次
			case 1:
				Date date= TimeUtil.convertString(value);
				tb.startAt(date);
				tb.withDescription(ContextUtil.getMessages("service.scheduler.execTimeByOnce")+":" + date.toLocaleString());
				break;
			//每分钟执行
			case 2:
				int minute=Integer.parseInt(value);
				ScheduleBuilder sb= CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMinutes(minute);
	
				tb.startNow();
				tb.withSchedule(sb);
				tb.withDescription(ContextUtil.getMessages("service.scheduler.minute",new Object[]{minute}));
				break;
			//每天时间点执行
			case 3:
				String[] aryTime=value.split(":");
				int hour=Integer.parseInt(aryTime[0]);
				int m=Integer.parseInt(aryTime[1]);
				ScheduleBuilder sb1=CronScheduleBuilder.dailyAtHourAndMinute(hour, m);
				tb.startNow();
				tb.withSchedule(sb1);
				tb.withDescription(ContextUtil.getMessages("service.scheduler.day",new Object[]{hour,m }));
				break;
			//每周时间点执行
			case 4:
				//0 15 10 ? * MON-FRI Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday 
				String[] aryExpression=value.split("[|]");
				String week=aryExpression[0];
				String[] aryTime1=aryExpression[1].split(":");
				String h1= aryTime1[0];
				String m1= aryTime1[1];
				String cronExperssion="0 "+ m1 +" " + h1 +" ? * " + week;
				ScheduleBuilder sb4=CronScheduleBuilder.cronSchedule(cronExperssion);
				tb.startNow();
				tb.withSchedule(sb4);
				String weekName=getWeek(week);
				tb.withDescription(ContextUtil.getMessages("service.scheduler.week",new Object[]{weekName,h1,m1}));
				break;
			//每月执行
			case 5:
				//0 15 10 15 * ?
				String[] aryExpression5=value.split("[|]");
				String day=aryExpression5[0];
				String[] aryTime2=aryExpression5[1].split(":");
				String h2= aryTime2[0];
				String m2= aryTime2[1];
				String cronExperssion1="0 "+ m2 +" " + h2 +" "+day+" * ?" ;
				ScheduleBuilder sb5=CronScheduleBuilder.cronSchedule(cronExperssion1);
				tb.startNow();
				tb.withSchedule(sb5);
				String dayName=getDay(day);
				tb.withDescription(ContextUtil.getMessages("service.scheduler.month",new Object[]{dayName,h2,m2}));
				break;
			//表达式
			case 6:
				try{
					ScheduleBuilder sb6=CronScheduleBuilder.cronSchedule(value);
					tb.startNow();
					tb.withSchedule(sb6);
				}catch(RuntimeException e){
					MessageUtil.addMsg(ContextUtil.getMessages("service.scheduler.cronTrigger.invalid"));
					throw e;
				}
				tb.withDescription(ContextUtil.getMessages("service.scheduler.cronTrigger")+":" + value);
				break;
		}
	}
	
	private String getDay(String day){
		String[] aryDay=day.split(",");
		int len=aryDay.length;
		String str="";
		for(int i=0;i<len;i++){
			String tmp=aryDay[i];
			tmp=tmp.equals("L")?ContextUtil.getMessages("service.scheduler.lastday"):tmp;
			if(i<len-1){
				str+=tmp +",";
			}
			else{
				str+=tmp;
			}
		}
		return str;
	}
	
	/**
	 * 取得星期名称
	 * @param week
	 * @return
	 */
	private String getWeek(String week){
		String[] aryWeek=week.split(",");
		int len=aryWeek.length;
		String str="";
		for(int i=0;i<len;i++){
			if(i<len-1)
				//str+=mapWeek.get(aryWeek[i]) +",";
				str+=ContextUtil.getMessages("service.scheduler."+aryWeek[i])+",";
			else
				//str+=mapWeek.get(aryWeek[i]);
				str+=ContextUtil.getMessages("service.scheduler."+aryWeek[i]);
		}
		return str;
	}
	
	
	
	/**
	 * 设置任务参数
	 * @param json
	 * @param jb
	 */
	private void setJobMap(String json,JobBuilder jb){
		JSONArray aryJson= JSONArray.fromObject(json);
		ParameterObject[] list=(ParameterObject[])aryJson.toArray(aryJson, ParameterObject.class);
		for(int i=0;i<list.length;i++){
			ParameterObject obj=(ParameterObject)list[i];
			String type=obj.getType();
			String name=obj.getName();
			String value=obj.getValue();
			if(type.equals("int")){
				if(StringUtil.isEmpty(value)){
					jb.usingJobData(name, 0);
				}else{
					jb.usingJobData(name, Integer.parseInt(value));
				}
			}
			else if(type.equals("long")){
				if(StringUtil.isEmpty(value)){
					jb.usingJobData(name, 0);
				}else{
					jb.usingJobData(name, Long.parseLong(value));
				}
			}
			else if(type.equals("float")){
				if(StringUtil.isEmpty(value)){
					jb.usingJobData(name, 0.0);
				}else{
					jb.usingJobData(name, Float.parseFloat(value));
				}
			}
			else if(type.equals("boolean")){
				if(StringUtil.isEmpty(value)){
					jb.usingJobData(name, false);
				}else{
					jb.usingJobData(name, Boolean.parseBoolean(value));
				}
			}
			else{
				jb.usingJobData(name,value);
			}
		}
	}
	
	/**
	 * 删除任务
	 * @param jobName
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public void delJob(String jobName) throws SchedulerException {
		if(scheduler==null ){
			return ;
		}
		JobKey key=new JobKey(jobName, schedGroup);
		scheduler.deleteJob(key);
	}

	public Trigger getTrigger(String triggerName) throws SchedulerException{
		if(scheduler==null ){
			return null;
		}
		TriggerKey key=new TriggerKey(triggerName, schedGroup);
		Trigger trigger= scheduler.getTrigger(key);
		return trigger;
	}
	
	/**
	 * 删除计划
	 * @param triggerName
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	public void delTrigger(String triggerName) throws SchedulerException {
		if(scheduler==null ){
			return ;
		}
		TriggerKey key=new TriggerKey(triggerName, schedGroup);
		scheduler.unscheduleJob(key);
	}
	
	/**
	 * 停止或暂停触发器
	 * @param triggerName
	 * @throws SchedulerException
	 */
	public void toggleTriggerRun(String triggerName) throws SchedulerException {
		if(scheduler==null ){
			return ;
		}
		TriggerKey key=new TriggerKey(triggerName, schedGroup);
		Trigger.TriggerState state= scheduler.getTriggerState(key);
		if(state==TriggerState.PAUSED){
			scheduler.resumeTrigger(key);
		}
		else if(state==TriggerState.NORMAL){
			scheduler.pauseTrigger(key);
		}
	}
	
	
	
	/**
	 * 直接执行任务
	 * @param jobName
	 * @throws SchedulerException
	 */
	public void executeJob(String jobName) throws SchedulerException{
		if(scheduler==null ){
			return ;
		}
		JobKey key=new JobKey(jobName, schedGroup);
		scheduler.triggerJob(key);
	}
	
	
	public boolean isInStandbyMode() throws SchedulerException {
		return this.scheduler.isInStandbyMode();
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
}