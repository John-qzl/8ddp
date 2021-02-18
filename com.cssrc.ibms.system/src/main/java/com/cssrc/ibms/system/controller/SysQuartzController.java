package com.cssrc.ibms.system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.core.job.quartz.BaseJob;
import com.cssrc.ibms.core.job.quartz.ParameterObject;
import com.cssrc.ibms.core.job.quartz.PlanObject;
import com.cssrc.ibms.core.job.quartz.SchedulerService;
import com.cssrc.ibms.core.job.quartz.TriggerObject;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;


/**
 * 定时计划管理
 * 
 * @author zhangxin
 *
 */
@Controller
@RequestMapping("/oa/system/quartz/")
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class SysQuartzController extends BaseController {
     @Resource
     private SchedulerFactoryBean schedulerFactoryBean;
     @Resource
 	 private SchedulerService schedulerService;

	/**
	 * 添加或更新定时任务
	 * 
	 * @param request
	 * @param response
	 * @param sysCodeTemplate
	 *            添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description = "添加或更新定时任务", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加新<#else>更新</#if>定时任务：")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		String name = RequestUtil.getString(request, "name");
		String jobClass = RequestUtil.getString(request, "jobClass");
		String description = RequestUtil.getString(request, "description");
		String parameterJson=RequestUtil.getString(request, "parameterJson");
		//执行类是否存在
		if(BeanUtils.validClass(jobClass)){
			Class cls = Class.forName(jobClass);
			//执行类是否继承了父类
			if(BeanUtils.isInherit(cls, BaseJob.class)){ 
				if(!schedulerService.isJobExists(name)){
					 
					schedulerService.addJob(name, jobClass, parameterJson, description);
					writeResultMessage(response.getWriter(), "添加定时任务",ResultMessage.Success);
				}else{
					//执行类未继承job接口
					writeResultMessage(response.getWriter(), "任务名称已被占用，请更换",ResultMessage.Fail);
				}

			}else{
				//执行类未继承job接口
				writeResultMessage(response.getWriter(), "该类没有继承BaseJob类",ResultMessage.Fail);
			}
		}else{
			//类不存在
			writeResultMessage(response.getWriter(), "该类不存在",ResultMessage.Fail);
		}
	}

	/**
	 * 取得定时任务分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "查看定时任务分页列表", detail = "查看定时任务分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SchedulerService schedulerService = new SchedulerService();
		schedulerService.setScheduler(schedulerFactoryBean.getScheduler());
		List<JobDetail> list = schedulerService.getJobList();
		ModelAndView mv = this.getAutoView().addObject("sysJobList", list);
		return mv;
	}

	/**
	 * 删除定时任务
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除定时任务", execOrder = ActionExecOrder.BEFORE, detail = "删除定时任务"
			+ "<#list StringUtils.split(id,\",\") as item>"
			+ "<#assign entity=sysJobService.getById(Long.valueOf(item))/>"
			+ "【${entity.name}】" + "</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			String name = RequestUtil.getString(request, "name");
			SchedulerService schedulerService = new SchedulerService();
			schedulerService.setScheduler(schedulerFactoryBean.getScheduler());
			schedulerService.delJob(name);
			message = new ResultMessage(ResultMessage.Success,
					getText("controller.del.success"));
		} catch (Exception ex) {
			message = new ResultMessage(ResultMessage.Fail,
					getText("controller.del.success") + ":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 编辑定时任务
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description = "添加或编辑定时任务", detail = "<#if isAdd>添加定时任务<#else>"
			+ "编辑定时任务"
			+ "<#assign entity=sysJobService.getById(Long.valueOf(id))/>"
			+ "【entity.name】" + "</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		String name = RequestUtil.getString(request, "name");
		JobDetail jobDetail = new JobDetailImpl();
		ModelAndView mv = getAutoView();
		if(!name.equals("")){
			SchedulerService schedulerService = new SchedulerService();
			schedulerService.setScheduler(schedulerFactoryBean.getScheduler());
		    jobDetail = schedulerService.getJob(name);
			JobDataMap map = jobDetail.getJobDataMap();
			List<ParameterObject> list = new ArrayList<ParameterObject>();
			for(Entry<String, Object> entry:map.entrySet()){
				String key = entry.getKey();
				String paramValue = null;
				String paramType = null;
				Object value = entry.getValue();
				if(value != null){
					paramValue = value.toString();
					//判断value值的类型来确定type：int、long、float、boolean、string
					String valueType = value.getClass().getSimpleName();
					if(valueType.equals("Integer")){
						paramType = "int";
					}else if(valueType.equals("Long")){
						paramType = "long";
					}else if(valueType.equals("Float")){
						paramType = "float";
					}else if(valueType.equals("Boolean")){
						paramType = "boolean";
					}else if(valueType.equals("String")){
						paramType = "string";
					}
				}
				ParameterObject obj = new ParameterObject();
				obj.setName(key);
				obj.setValue(paramValue);
				obj.setType(paramType);
				list.add(obj);
			}
			mv.addObject("paramList", list);
		}
		mv.addObject("jobDetail", jobDetail);
		mv.addObject("returnUrl", returnUrl);
		return mv;
	}

	/**
	 * 检查任务是否存在
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 * @see
	 */
	@RequestMapping("checkSysJob")
	public void checkSysJob(HttpServletRequest request,HttpServletResponse response) throws IOException, ClassNotFoundException{
		String jobClass = RequestUtil.getString(request, "jobClass");
		if(!BeanUtils.validClass(jobClass)){
			writeResultMessage(response.getWriter(), "任务类不存在",ResultMessage.Fail);
		}else{
			Class cls = Class.forName(jobClass);
			if(!BeanUtils.isInherit(cls, BaseJob.class)){
				writeResultMessage(response.getWriter(), "该类没有继承BaseJob类",ResultMessage.Fail);
			}else{
				writeResultMessage(response.getWriter(), "任务类可用",ResultMessage.Success);
			}
		}
	}
	/**
	 * 编辑执行计划
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @see
	 */
	@RequestMapping("planeEdit")
	@Action(description = "添加或编辑执行计划", detail = "<#if isAdd>添加执行计划<#else>"
			+ "编辑执行计划"
			+ "<#assign entity=sysJobPlaneService.getById(Long.valueOf(id))/>"
			+ "【entity.name】" + "</#if>")
	public ModelAndView planeEdit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		String name = RequestUtil.getString(request, "name");
		TriggerObject obj = new TriggerObject();
		obj.setJobName(name);
		ModelAndView mv = getAutoView();
		mv.addObject("TriggerObject", obj);
		mv.addObject("returnUrl", returnUrl);
		return mv;
	}

	/**
	 * 添加或更新执行计划
	 * 
	 * @param request
	 * @param response
	 * @param sysJobPlane
	 * @throws Exception
	 * @see
	 */
	@RequestMapping("planeSave")
	@Action(description = "添加或更新执行计划", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加新<#else>更新</#if>执行计划：")
	public void planeSave(HttpServletRequest request,
			HttpServletResponse response, TriggerObject triggerObject)
			throws Exception {
		SchedulerService schedulerService = new SchedulerService();
		schedulerService.setScheduler(schedulerFactoryBean.getScheduler());
		if(!schedulerService.isTriggerExists(triggerObject.getName())){
			PlanObject object = new PlanObject();
			object.setType(6);
			object.setTimeInterval(triggerObject.getCron());
			schedulerService.addTrigger(triggerObject.getJobName(), triggerObject.getName(),JSONObject.fromObject(object).toString() );
			writeResultMessage(response.getWriter(), "添加执行计划",ResultMessage.Success);
		}else{
			writeResultMessage(response.getWriter(), "执行计划名称已被占用",ResultMessage.Fail);
		}
	}

	/**
	 * 查看执行计划分页列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @see
	 */
	@RequestMapping("planeList")
	@Action(description = "查看执行计划分页列表", detail = "查看执行计划分页列表")
	public ModelAndView planeList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String name = RequestUtil.getString(request, "name");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		SchedulerService schedulerService = new SchedulerService();
		schedulerService.setScheduler(scheduler);
		List<TriggerObject> objList = new ArrayList<TriggerObject>();
		List<Trigger> list = schedulerService.getTriggersByJob(name);
		for(Trigger trigger : list ){
			String cron = ((CronTriggerImpl)trigger).getCronExpression();
			TriggerState  triggerState  = scheduler.getTriggerState(trigger.getKey());
			TriggerObject obj = new TriggerObject();
			obj.setName(trigger.getKey().getName());
			obj.setGroup(trigger.getKey().getGroup());
			obj.setCron(cron);
			obj.setStatus(triggerState.name());
			obj.setCronDescription(parseCron(cron));
			objList.add(obj);
			System.out.print(triggerState.name());
		}
		ModelAndView mv = this.getAutoView()
				.addObject("sysJobPlaneList", objList)
				.addObject("quartzName", name);
		return mv;
	}

	/**
	 * 删除执行计划
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @see
	 */
	@RequestMapping("planeDel")
	@Action(description = "删除执行计划", execOrder = ActionExecOrder.BEFORE, detail = "删除执行计划"
			+ "<#list StringUtils.split(id,\",\") as item>"
			+ "<#assign entity=sysJobPlaneService.getById(Long.valueOf(item))/>"
			+ "【${entity.name}】" + "</#list>")
	public void planeDel(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			String name = RequestUtil.getString(request, "name");
			SchedulerService schedulerService = new SchedulerService();
			schedulerService.setScheduler(schedulerFactoryBean.getScheduler());
			schedulerService.delTrigger(name);
			message = new ResultMessage(ResultMessage.Success,getText("controller.del.success"));
		} catch (Exception ex) {
			message = new ResultMessage(ResultMessage.Fail,getText("controller.del.success") + ":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 启动定时计划
	 * 		   [修改时间] 2015年6月2日 上午9:54:35
	 * @param request
	 * @param response
	 * @throws SchedulerException
	 * @throws IOException
	 * @see
	 */
	@RequestMapping("planeStart")
	public void planeStart(HttpServletRequest request,
			HttpServletResponse response) throws SchedulerException, IOException{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		String name = RequestUtil.getString(request, "name");
		SchedulerService schedulerService = new SchedulerService();
		schedulerService.setScheduler(schedulerFactoryBean.getScheduler());
		schedulerService.toggleTriggerRun(name);
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	/**
	 * 暂停定时计划
	 * @param request
	 * @param response
	 * @throws SchedulerException
	 * @throws IOException
	 * @see
	 */
	@RequestMapping("planePause")
	public void planePause(HttpServletRequest request,
			HttpServletResponse response) throws SchedulerException, IOException{
		String preUrl = RequestUtil.getPrePage(request);
		String name = RequestUtil.getString(request, "name");
		SchedulerService schedulerService = new SchedulerService();
		schedulerService.setScheduler(schedulerFactoryBean.getScheduler());
		schedulerService.toggleTriggerRun(name);
		response.sendRedirect(preUrl);
	}
	/**
	 * 执行任务
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws SchedulerException 
	 */
	@RequestMapping("executeJob")
	public void executeJob(HttpServletRequest request, HttpServletResponse response) throws IOException, SchedulerException{

		String jobName=RequestUtil.getString(request,"name");
		schedulerService.executeJob(jobName);
		response.sendRedirect(RequestUtil.getPrePage(request));
	}
	
	/**
	 * 将cron表达式解析为中文
	 * @author liubo
	 * @param cronEx cron表达式
	 * @return
	 */
	public String parseCron(String cronEx){
		//年月日周时分秒的定义
		String second = "00";
		String minute = "00";
		String hour = "0";
		String day = "0";
		String week = "0";
		String mouth = "0";
		String year = "0";
		
		String cronDescription = cronEx;
		String[] crons = cronEx.split(" ");
		
		if(!crons[0].equals("0")){//一次的情况
			if(crons[0].length()==1)
				second = "0"+crons[0];
			else
				second = crons[0];
			
			if(!crons[1].equals("0")&&crons[1].length()==1)
				minute = "0"+crons[1];
			else if(crons[1].length()==2)
				minute = crons[1];
			hour = crons[2];
			day = crons[3];
			mouth = crons[4];
			year = crons[6];
			cronDescription = "只在"+year+"-"+mouth+"-"+day+" "+hour+":"+minute+":"+second+"执行一次";
		}else{
			if(crons[1].contains("/")){//每隔多少分钟执行一次
				String[] minutes = crons[1].split("/");
				minute = minutes[1];
				cronDescription = "每隔"+minute+"分钟执行一次";
			}else if(crons[5].equals("?")){
				if(!crons[1].equals("0")&&crons[1].length()==1)
					minute = "0"+crons[1];
				else if(crons[1].length()==2)
					minute = crons[1];
				hour = crons[2];
				if(crons[3].equals("*")){//每天什么时候执行一次
					cronDescription = "每天的"+hour+":"+minute+"执行一次";
				}else{//每月的某几天执行
					day = crons[3];
					cronDescription = "每月的"+day+"日的"+hour+":"+minute+"执行一次";
				}
			}else{//每周
				if(!crons[1].equals("0")&&crons[1].length()==1)
					minute = "0"+crons[1];
				else if(crons[1].length()==2)
					minute = crons[1];
				hour = crons[2];
				week = crons[5];
				cronDescription = "每周"+week+"的"+hour+":"+minute+"执行一次";
			}
			
		}
		return cronDescription;
	} 

}
