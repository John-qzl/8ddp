package com.cssrc.ibms.system.controller;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.model.Job;
import com.cssrc.ibms.system.service.DictionaryService;
import com.cssrc.ibms.system.service.JobService;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 职务控制层取代sysjob
 * <p>Title:JobController</p>
 * @author Yangbo 
 * @date 2016-8-4下午03:32:41
 */
@Controller
@RequestMapping({"/oa/system/job/"})
@Action(ownermodel = SysAuditModelType.JOB_MANAGEMENT)
public class JobController extends BaseController
{
	@Resource
	private JobService jobService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private IPositionService positionService;
	
	@RequestMapping({"save"})
	@Action(description="添加或更新职务", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>职务 ${SysAuditLinkService.getJobLink(Long.valueOf(id))}",exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { Job.class }, pkName = "jobid")
	public void save(HttpServletRequest request, HttpServletResponse response, Job job)
	throws Exception
	{
		String resultMsg = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		boolean isAdd = true;
		String id = null;
		try {
			boolean isExistJobName = false;
			if ((job.getJobid() == null) || (job.getJobid().longValue() == 0L)) {
				job.setJobid(Long.valueOf(UniqueIdUtil.genId()));
				isExistJobName = this.jobService.isExistJobCode(job.getJobcode());
				if (!isExistJobName) {
					job.setJob_creatorId(UserContextUtil.getCurrentUserId());
					job.setJob_createTime(new Date());
					job.setJob_updateId(UserContextUtil.getCurrentUserId());
					job.setJob_updateTime(new Date());
					this.jobService.add(job);
					result = 1;
					resultMsg = "添加职务成功";
					id = job.getJobid().toString();
				} else {
					resultMsg = "职务名称已经存在！";
					id = jobService.getByJobCode(job.getJobcode()).getJobid().toString();
				}
			}
			else {
				isExistJobName = this.jobService.isExistJobCodeForUpd(job.getJobcode(), job.getJobid());
				if (isExistJobName) {
					resultMsg = "职务名称已经存在！";
					id = jobService.getByJobCode(job.getJobcode()).getJobid().toString();
				}
				else {
					job.setJob_updateId(UserContextUtil.getCurrentUserId());
					job.setJob_updateTime(new Date());
					this.jobService.update(job);
					result = 1;
					isAdd = false;
					resultMsg = "更新职务成功";
					id = job.getJobid().toString();
				}
			}
			if (isExistJobName)
				writeResultMessage(response.getWriter(), resultMsg, 0);
			else
				writeResultMessage(response.getWriter(), resultMsg, 1);
		}
		catch (DuplicateKeyException ex)
		{
			writeResultMessage(response.getWriter(), "该职务代码已存在.", 0);
		}
		catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), 0);
		}
		try {
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.putParamerter("id", id);
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isAdd));
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@RequestMapping({"list"})
	@Action(description="查看职务表分页列表", detail="查看职务表分页列表",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		List list = this.jobService.getAll(new QueryFilter(request, "jobItem"));
		ModelAndView mv = getAutoView().addObject("jobList", list);

		return mv;
	}

	@RequestMapping({"del"})
	@Action(description="删除职务表",execOrder = ActionExecOrder.BEFORE,detail="删除职务 <#list jobid?split(\",\") as item><#assign entity=jobService.getById(Long.valueOf(item))/> ${entity.jobname}【${entity.jobcode}】</#list>",exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try
		{
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "jobid");

			int i = 0; for (int n = lAryId.length; i < n; i++) {
				if (this.positionService.isJobUsedByPos(lAryId[i])) {
					message = new ResultMessage(0, "职务被岗位使用，不能删除！");
					break;
				}
				this.jobService.deleteByUpdateFlag(lAryId[i]);
				message = new ResultMessage(1, "删除职务表成功!");
			}
		}
		catch (Exception ex)
		{
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({"edit"})
	public ModelAndView edit(HttpServletRequest request)
	throws Exception
	{
		Long jobid = Long.valueOf(RequestUtil.getLong(request, "jobid", 0L));
		String returnUrl = RequestUtil.getPrePage(request);
		Job job = (Job)this.jobService.getById(jobid);
		List dicList = this.dictionaryService.getByNodeKey("zwjb");

		return getAutoView().addObject("job", job)
		.addObject("returnUrl", returnUrl)
		.addObject("dicList", dicList);
	}

	@RequestMapping({"get"})
	@Action(description="查看职务表明细",detail="查看职务表明细",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long jobid = Long.valueOf(RequestUtil.getLong(request, "jobid"));
		Job job = (Job)this.jobService.getById(jobid);
		return getAutoView().addObject("job", job);
	}

	@RequestMapping({"dialog"})
	@Action(description="职务对话框显示", execOrder=ActionExecOrder.AFTER, detail="职务对话框显示", exectype=SysAuditExecType.SELECT_TYPE)
	public ModelAndView dialog(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String isSingle = RequestUtil.getString(request, "isSingle");
		return getAutoView().addObject("isSingle", isSingle);
	}

	@RequestMapping({"selector"})
	@Action(description="职务对话框显示", execOrder=ActionExecOrder.AFTER, detail="职务对话框显示", exectype=SysAuditExecType.SELECT_TYPE)
	public ModelAndView selector(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String isSingle = RequestUtil.getString(request, "isSingle", "false");
		QueryFilter queryFilter = new QueryFilter(request, "jobItem");
		String jobName = RequestUtil.getString(request, "Q_jobname_S");
		if (StringUtil.isNotEmpty(jobName)) {
			queryFilter.addFilterForIB("jobname", "%" + jobName + "%");
		}
		List list = this.jobService.getAll(queryFilter);
		ModelAndView mv = getAutoView()
		.addObject("jobList", list)
		.addObject("isSingle", isSingle);
		return mv;
	}
}
