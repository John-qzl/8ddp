package com.cssrc.ibms.system.controller;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.job.quartz.SchedulerService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.intf.ISysJobLogService;
import com.cssrc.ibms.api.log.model.ISysJobLog;
/**
 * 
 * <p>Title:SchedulerController</p>
 * @author Yangbo 
 * @date 2016-8-4下午05:31:19
 */
@Controller
@RequestMapping( { "/oa/system/scheduler/" })
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class SchedulerController extends BaseController {

	@Resource
	SchedulerService schedulerService;
	@Resource
	ISysJobLogService sysJobLogService;

	@RequestMapping( { "/addJob{viewName}" })
	@Action(description = "添加定时计划作业", execOrder = ActionExecOrder.AFTER, detail = "<#if STEP1>进入 添加定时计划作业  编辑页面<#else>添加定时计划【${name}】</#if>")
	public ModelAndView addJob(HttpServletResponse response,
			HttpServletRequest request,
			@PathVariable("viewName") String viewName) throws Exception {
		try {
			LogThreadLocalHolder.putParamerter("STEP1", Boolean.valueOf("1"
					.equals(viewName)));
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}

		PrintWriter out = response.getWriter();
		ModelAndView mv = new ModelAndView();
		if ("1".equals(viewName)) {
			mv.setViewName("/oa/scheduler/jobAdd.jsp");
			return mv;
		}
		if ("2".equals(viewName)) {
			try {
				String className = RequestUtil.getString(request, "className");
				String jobName = RequestUtil.getString(request, "name");
				String parameterJson = RequestUtil.getString(request,
						"parameterJson");
				String description = RequestUtil.getString(request,
						"description");
				boolean isExist = this.schedulerService.isJobExists(jobName);
				if (isExist) {
					ResultMessage obj = new ResultMessage(0, "任务名称已经存在，添加失败!");
					out.print(obj.toString());
				} else {
					this.schedulerService.addJob(jobName, className,
							parameterJson, description);
					ResultMessage obj = new ResultMessage(1, "添加任务成功!");
					out.print(obj.toString());
				}
			} catch (ClassNotFoundException ex) {
				ResultMessage obj = new ResultMessage(0, "添加指定的任务类不存在，添加失败!");
				out.print(obj.toString());
			} catch (Exception ex) {
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					ResultMessage resultMessage = new ResultMessage(0,
							"添加任务失败:" + str);
					response.getWriter().print(resultMessage);
				} else {
					String message = ExceptionUtil.getExceptionMessage(ex);
					ResultMessage resultMessage = new ResultMessage(0, message);
					response.getWriter().print(resultMessage);
				}
			}
			return null;
		}
		return null;
	}

	@RequestMapping( { "getJobList" })
	public ModelAndView getJobList(HttpServletResponse response,
			HttpServletRequest request) throws SchedulerException {
		boolean isInStandbyMode = this.schedulerService.isInStandbyMode();
		List list = this.schedulerService.getJobList();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/oa/scheduler/jobList.jsp");
		mv.addObject("jobList", list);
		mv.addObject("isStandby", Boolean.valueOf(isInStandbyMode));
		return mv;
	}

	@RequestMapping( { "/delJob" })
	@Action(description = "删除定时计划作业", detail = "删除定时计划作业<#list StringUtils.split(jobName,\",\") as item>【${item}】</#list>")
	public void delJob(HttpServletResponse response, HttpServletRequest request)
			throws IOException, SchedulerException, ClassNotFoundException {
		ResultMessage message = null;
		try {
			String jobName = RequestUtil.getString(request, "jobName");
			this.schedulerService.delJob(jobName);
			message = new ResultMessage(1, "删除任务成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除任务失败:" + e.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(RequestUtil.getPrePage(request));
	}

	@RequestMapping( { "/addTrigger{viewName}" })
	@Action(description = "添加定时计划", execOrder = ActionExecOrder.AFTER, detail = "<#if STEP1>进入 添加定时计划  编辑页面<#else>添加定时计划作业【${jobName}】计划【${name}】</#if>")
	public ModelAndView addTrigger(HttpServletResponse response,
			HttpServletRequest request,
			@PathVariable("viewName") String viewName) throws IOException,
			SchedulerException, ParseException {
		try {
			LogThreadLocalHolder.putParamerter("STEP1", Boolean.valueOf("1"
					.equals(viewName)));
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}

		PrintWriter out = response.getWriter();
		ModelAndView mv = new ModelAndView();
		if ("1".equals(viewName)) {
			String jobName = RequestUtil.getString(request, "jobName");
			mv.setViewName("/oa/scheduler/triggerAdd.jsp");
			mv.addObject("jobName", jobName);
			return mv;
		}
		if ("2".equals(viewName)) {
			String trigName = RequestUtil.getString(request, "name");
			String jobName = RequestUtil.getString(request, "jobName");

			String planJson = RequestUtil.getString(request, "planJson");

			boolean rtn = this.schedulerService.isTriggerExists(trigName);
			if (rtn) {
				ResultMessage obj = new ResultMessage(0, "指定的计划名称已经存在!");
				out.print(obj.toString());
			}
			try {
				this.schedulerService.addTrigger(jobName, trigName, planJson);
				ResultMessage obj = new ResultMessage(1, "添加计划成功!");
				out.print(obj.toString());
			} catch (SchedulerException e) {
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					ResultMessage resultMessage = new ResultMessage(0,
							"添加计划失败:" + str);
					response.getWriter().print(resultMessage);
				} else {
					String message = ExceptionUtil.getExceptionMessage(e);
					ResultMessage resultMessage = new ResultMessage(0, message);
					response.getWriter().print(resultMessage);
				}
			}
			return null;
		}
		return null;
	}

	@RequestMapping( { "/getTriggersByJob" })
	public ModelAndView getTriggersByJob(HttpServletResponse response,
			HttpServletRequest request) throws SchedulerException {
		String jobName = RequestUtil.getString(request, "jobName");

		List list = this.schedulerService.getTriggersByJob(jobName);
		HashMap mapState = this.schedulerService.getTriggerStatus(list);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/oa/scheduler/triggerList.jsp");
		mv.addObject("list", list);
		mv.addObject("mapState", mapState);
		mv.addObject("jobName", jobName);

		return mv;
	}

	@RequestMapping( { "executeJob" })
	public void executeJob(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			SchedulerException {
		String jobName = RequestUtil.getString(request, "jobName");
		this.schedulerService.executeJob(jobName);
		response.sendRedirect(RequestUtil.getPrePage(request));
	}

	@RequestMapping( { "validClass" })
	public void validClass(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String className = RequestUtil.getString(request, "className", "");
		boolean rtn = BeanUtils.validClass(className);
		if (rtn) {
			ResultMessage obj = new ResultMessage(1, "验证类成功!");
			out.println(obj.toString());
		} else {
			ResultMessage obj = new ResultMessage(0, "验证类失败!");
			out.println(obj.toString());
		}
	}

	@RequestMapping( { "/delTrigger" })
	@Action(description = "删除定时计划作业计划", execOrder = ActionExecOrder.BEFORE, detail = "删除定时计划作业计划：<#list StringUtils.split(name,\",\") as item><#assign entity=SysAuditLinkService.getTrigger(item)/>【作业：${entity.jobKey.name}，计划：${item}】</#list>")
	public void delTrigger(HttpServletResponse response,
			HttpServletRequest request) throws IOException, SchedulerException,
			ClassNotFoundException {
		ResultMessage message = null;
		try {
			String name = RequestUtil.getString(request, "name");
			this.schedulerService.delTrigger(name);
			message = new ResultMessage(1, "删除计划成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除计划失败:" + e.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(RequestUtil.getPrePage(request));
	}

	@RequestMapping( { "/toggleTriggerRun" })
	@Action(description = "启用或禁用定时计划作业计划", execOrder = ActionExecOrder.AFTER, detail = "设置定时计划作业【${jobName}】的计划状态：<#list StringUtils.split(triggerName,\",\") as item>【${item}：\t<#if TriggerState.NORMAL==striggerStatus[item]>启用 \t<#elseif TriggerState.PAUSED==striggerStatus[item]>禁用  \t<#elseif TriggerState.ERROR==striggerStatus[item]>执行出错 \t<#elseif TriggerState.COMPLETE==striggerStatus[item]>完成 \t<#elseif TriggerState.BLOCKED==striggerStatus[item]>正在执行 \t<#elseif TriggerState.NONE==striggerStatus[item]>未启动 \t<#elseif TriggerState.PAUSED==striggerStatus[item]>禁用\t<#else>未知</#if>】</#list>")
	public void toggleTriggerRun(HttpServletResponse response,
			HttpServletRequest request) throws IOException, SchedulerException {
		String name = RequestUtil.getString(request, "name");
		this.schedulerService.toggleTriggerRun(name);
		response.sendRedirect(RequestUtil.getPrePage(request));
	}

	@RequestMapping( { "getLogList" })
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String jobName = RequestUtil.getString(request, "jobName");
		String trigName = RequestUtil.getString(request, "trigName");
		List<?extends ISysJobLog> list = this.sysJobLogService.getAll(new QueryFilter(request,
				"sysJobLogItem"));
		ModelAndView mv = new ModelAndView("/oa/scheduler/sysJobLogList.jsp");
		mv.addObject("sysJobLogList", list);
		mv.addObject("jobName", jobName);
		mv.addObject("trigName", trigName);

		return mv;
	}

	@RequestMapping( { "delJobLog" })
	@Action(description = "删除定时计划作业执行日志", execOrder = ActionExecOrder.BEFORE, detail = "删除定时计划作业执行日志<#list StringUtils.split(logId,\",\") as item><#assign entity=sysJobLogService.getById(Long.valueOf(item))/>【作业：${entity.jobName}，计划：${entity.trigName}，内容：${entity.content}】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage message = null;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "logId");
			this.sysJobLogService.delByIds(lAryId);
			message = new ResultMessage(1, "删除任务日志成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除任务日志失败:" + e.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "changeStart" })
	public void changeStart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		String resultMsg = "";
		boolean isStandby = RequestUtil.getBoolean(request, "isStandby");
		try {
			if (isStandby) {
				this.schedulerService.start();
				resultMsg = "启动定时器成功!";
			} else {
				this.schedulerService.shutdown();
				resultMsg = "停止定时器成功!";
			}
			message = new ResultMessage(1, resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
			if (isStandby)
				resultMsg = "启动定时器失败:";
			else {
				resultMsg = "停止定时器失败:";
			}
			message = new ResultMessage(0, resultMsg + e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}
}
