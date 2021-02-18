package com.cssrc.ibms.core.flow.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.ISysTemplateService;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.excel.util.ExcelUtil;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.RunLog;
import com.cssrc.ibms.core.flow.model.TaskExe;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.RunLogService;
import com.cssrc.ibms.core.flow.service.TaskExeService;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.api.jms.intf.IMessageHandler;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;

/**
 * <pre>
 * 对象功能:任务转办代理 控制器类 
 * 开发人员:zhulongchao 
 * </pre>
 */
@Controller
@RequestMapping("/oa/flow/taskExe/")
@Action(ownermodel=SysAuditModelType.FLOW_MANAGEMENT)
public class TaskExeController extends BaseController {
	@Resource
	private TaskExeService taskExeService;
	@Resource
	private IBpmService bpmService;
	@Resource
	private ProcessRunService processRunService;
	@Resource
	private DefinitionService definitionService;
	@Resource
	private RunLogService runLogService;
	@Resource
	private ISysTemplateService sysTemplateService;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	Map<String, IMessageHandler> handlersMap;
	
	


	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("accordingMatters")
	public ModelAndView accordingMatters(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = getAutoView();
		return result;
	}

	/**
	 * 添加或更新任务转办代理。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("assignSave")
	@Action(description = "添加任务转办代理",execOrder=ActionExecOrder.AFTER,
			detail=
					"<#if StringUtil.isNotEmpty(isSuccess)>" +
					"添加任务【${SysAuditLinkService.getProcessRunDetailLink(taskId)}】转办代理给【${SysAuditLinkService.getSysUserLink(Long.valueOf(assigneeId))}】" +
					"成功" +
					"<#else>添加任务转办代理失败</#if>")
	public void assignSave(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String resultMsg = null;
		try {
			String taskId = RequestUtil.getString(request, "taskId");
			Long assigneeId = RequestUtil.getLong(request, "assigneeId");
			String assigneeName = RequestUtil.getString(request, "assigneeName");
			String memo = RequestUtil.getString(request, "memo");
			
			String informType = RequestUtil.getStringAry(request, "informType");
		
			
			ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
			TaskEntity taskEntity = bpmService.getTask(taskId);
			if (BeanUtils.isEmpty(taskEntity)){
				writeResultMessage(response.getWriter(), getText("controller.bpmTaskExe.dealWith"),ResultMessage.Fail);
				return;
			}
			String assignee = taskEntity.getAssignee();
			//任务人不为空且和当前用户不同。
			if(FlowUtil.isAssigneeNotEmpty(assignee) && assignee.equals(assigneeId)){
				writeResultMessage(response.getWriter(), getText("controller.bpmTaskExe.notAssignee"),ResultMessage.Fail);
				return;
			}
			if(FlowUtil.isAssigneeNotEmpty(assignee) ){	
				if(!assignee.equals(sysUser.getUserId().toString())){
					writeResultMessage(response.getWriter(),getText("controller.bpmTaskExe.notExecutors"),ResultMessage.Fail);
					return;
				}
			}
			
			
			ProcessRun processRun = processRunService.getByActInstanceId(new Long( taskEntity.getProcessInstanceId()));
			
			String actDefId=processRun.getActDefId();
			
			boolean rtn= definitionService.allowDivert(actDefId);
			if(!rtn){
				writeResultMessage(response.getWriter(), getText("controller.bpmTaskExe.notAllowAssignee"),ResultMessage.Fail);
				return;
			}
			
			boolean isAssign=taskExeService.getByIsAssign(new Long(taskId));
			if(isAssign){
				writeResultMessage(response.getWriter(), getText("controller.bpmTaskExe.hasAssignee"),ResultMessage.Fail);
				return;
			}
			
			
			TaskExe bpmTaskExe = new TaskExe();
			bpmTaskExe.setId(UniqueIdUtil.genId());
			bpmTaskExe.setTaskId(new Long(taskId));
			bpmTaskExe.setAssigneeId(assigneeId);
			bpmTaskExe.setAssigneeName(assigneeName);
			bpmTaskExe.setOwnerId(sysUser.getUserId());
			bpmTaskExe.setOwnerName(sysUser.getFullname());
			bpmTaskExe.setSubject(processRun.getSubject());
			bpmTaskExe.setStatus(TaskExe.STATUS_INIT);
			bpmTaskExe.setMemo(memo);
			bpmTaskExe.setCratetime(new Date());
			bpmTaskExe.setActInstId(new Long(taskEntity.getProcessInstanceId()));
			bpmTaskExe.setTaskDefKey(taskEntity.getTaskDefinitionKey());
			bpmTaskExe.setTaskName(taskEntity.getName());
			bpmTaskExe.setAssignType(TaskExe.TYPE_TRANSMIT);
			bpmTaskExe.setRunId(processRun.getRunId());
			bpmTaskExe.setTypeId(processRun.getTypeId());
			bpmTaskExe.setInformType(informType);
			taskExeService.assignSave(bpmTaskExe);
			LogThreadLocalHolder.putParamerter("isSuccess", "true");
			writeResultMessage(response.getWriter(), getText("controller.bpmTaskExe.success"),ResultMessage.Success);
		} 
		catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(),resultMsg + "," + e.getMessage(), ResultMessage.Fail);
		}
		
	}

	/**
	 * 转办事宜
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = " 转办事宜")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request, "bpmTaskExeItem");

		String nodePath = RequestUtil.getString(request, "nodePath");
		if(StringUtils.isNotEmpty(nodePath))
			filter.getFilters().put("nodePath",nodePath + "%");
		filter.addFilterForIB("exceptDefStatus", 3);
		List<TaskExe> list = taskExeService.accordingMattersList(filter);
		ModelAndView mv = this.getAutoView().addObject("bpmTaskExeList", list);

		return mv;
	}
	/**
	 * 全部转办代理任务导出
	 * @param request
	 * @param response
	 */
	@RequestMapping("downloadAllAccordingMattersList")
	@Action(description="全部转办代理任务导出")
	public void downloadAllAccordingMattersList(HttpServletRequest request,HttpServletResponse response){
		QueryFilter filter = new QueryFilter(request, "bpmTaskExeItem");
		Long userId = UserContextUtil.getCurrentUserId();
		filter.addFilterForIB("ownerId", userId);
		String nodePath = RequestUtil.getString(request, "nodePath");
		if(StringUtils.isNotEmpty(nodePath)){
			filter.getFilters().put("nodePath",nodePath + "%");
		}
		String[] headers={"序号","请求标题","流程名称","创建人","转办(代理)人","创建时间","转办(代理)类型","待办类型"};
		List<TaskExe> list = taskExeService.accordingMattersList(filter);
		List<Object[]> vals=new ArrayList<Object[]>();
		for(int i=0;i<list.size();i++){
			TaskExe taskExe=list.get(i);
			Object[] objs=new Object[headers.length];
			objs[0]=i+1;
			objs[1]=taskExe.getSubject();
			objs[2]=taskExe.getProcessName();
			objs[3]=taskExe.getCreator();
			objs[4]=taskExe.getAssigneeName();
			objs[5]=taskExe.getCratetime();
			objs[6]=getAccordingMatterType(taskExe.getStatus(),taskExe.getAssignType());
			objs[7]=getAssignType(taskExe.getAssignType());
			vals.add(objs);
		}
		ExcelUtil.exportToExcel("全部转办代理任务", headers, vals, response);
	}
	
	private Object getAssignType(Short assignType) {
		switch (assignType) {
		case 1:
			return "代理";
		case 2:
			return "转办";
		case 4:
			return "多级转办";
		default:
			break;
		}
		return "";
	}

	private Object getAccordingMatterType(Short status, Short assignType) {
		switch (status) {
		case 0:
			return "初始状态";
		case 1:
			return "完成任务";
		case 2:
			switch(assignType){
			case 1:
				return "取消代理";
			case 2:
				return "取消转办";
			}
		case 3:
			return "其他";
		default:
			break;
		}
		return "";
	}

	/**
	 * 转办事宜
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("accordingMattersList")
	@Action(description = " 转办事宜")
	public ModelAndView accordingMattersList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request, "bpmTaskExeItem");
		Long userId = UserContextUtil.getCurrentUserId();
		filter.addFilterForIB("ownerId", userId);
		String nodePath = RequestUtil.getString(request, "nodePath");
		if(StringUtils.isNotEmpty(nodePath))
			filter.getFilters().put("nodePath",nodePath + "%");
		
		List<TaskExe> list = taskExeService.accordingMattersList(filter);
		ModelAndView mv = this.getAutoView().addObject("bpmTaskExeList", list);

		return mv;
	}

	/**
	 * 取得我收到的任务分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("myTaskList")
	@Action(description = "查看我收到的任务")
	public ModelAndView myTaskList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request, "bpmTaskExeItem");
		filter.addFilterForIB("assigneeId", UserContextUtil.getCurrentUserId());
		filter.addFilterForIB("assignType",TaskExe.TYPE_TRANSMIT);
		List<TaskExe> list = taskExeService.getAll(filter);
		ModelAndView mv = this.getAutoView().addObject("bpmTaskExeList", list);

		return mv;
	}
	
	/**
	 * 取消转办
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("cancel")
	@Action(description = "取消转办/代理")
	public void cancel(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long id = RequestUtil.getLong(request, "id");
		String opinion=RequestUtil.getString(request, "opinion");
		String informType=RequestUtil.getString(request, "informType");
//		ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();

		String bpmTaskExeType=null;
		
		
		try {
			TaskExe bpmTaskExe = taskExeService.getById(id);
			
			if(TaskExe.TYPE_ASSIGNEE.shortValue() == bpmTaskExe.getAssignType()){
				bpmTaskExeType=getText("controller.bpmTaskExe.cancel.assignee");
			}else if(TaskExe.TYPE_TRANSMIT.shortValue() == bpmTaskExe.getAssignType()){
				bpmTaskExeType=getText("controller.bpmTaskExe.cancel.transmit");
			}else if(TaskExe.TYPE_TRANSTO.shortValue() == bpmTaskExe.getAssignType()){
				bpmTaskExeType=getText("controller.bpmTaskExe.cancel.trans");;
			}
			Object [] args = {bpmTaskExeType};
			Long taskId= bpmTaskExe.getTaskId();
			TaskEntity taskEntity=bpmService.getTask(taskId.toString());
			if(taskEntity==null){
				writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Fail, getText("controller.bpmTaskExe.cancel.taskEnd")));
				return ;
			}
			Long ownerId = bpmTaskExe.getOwnerId();
			if(ownerId==null){
				writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Fail, getText("controller.bpmTaskExe.cancel.origExecutors")));
				return ;
			}
			ISysUser sysUser=sysUserService.getById(ownerId);
			if(sysUser==null){
				writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Fail, getText("controller.bpmTaskExe.cancel.origExecutors")));
				return ;
			}
			//取消转办/代理。
			taskExeService.cancel(bpmTaskExe, sysUser,opinion,informType);
			// 记录日志
			ProcessRun processRun = processRunService.getByActInstanceId(bpmTaskExe.getActInstId());
			String memo = getText("controller.bpmTaskExe.cancel.flowMemo",processRun.getSubject())+ "," +getText("controller.bpmTaskExe.addRunLong.memo",sysUser.getFullname(),bpmTaskExe.getSubject());
			runLogService.addRunLog(processRun.getRunId(),RunLog.OPERATOR_TYPE_BACK, memo);
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Success, getText("controller.bpmTaskExe.cancel.success",args)));
		} catch (Exception e) {
			Object [] args = {bpmTaskExeType};
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(
						ResultMessage.Fail, getText("controller.bpmTaskExe.cancel.fail",args) + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(
						ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}

	}
	
	private void addRunLong(List<TaskExe> list){
		ISysUser sysUser=(ISysUser)UserContextUtil.getCurrentUser();
		for(TaskExe bpmTaskExe:list){
			String memo =getText("controller.bpmTaskExe.addRunLong.memo",sysUser.getFullname(),bpmTaskExe.getSubject());
			runLogService.addRunLog(bpmTaskExe.getRunId(),RunLog.OPERATOR_TYPE_BACK, memo);
		}
	}

	/**
	 * 批量取消转办
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("cancelBat")
	@Action(description = "批量取消转办")
	public void cancelBat(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
			String idStr = RequestUtil.getString(request, "ids");
			String opinion=RequestUtil.getString(request, "opinion");
			String informType=RequestUtil.getString(request, "informType");
			if(StringUtil.isEmpty(idStr)){
				writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Fail, getText("controller.bpmTaskExe.cancelBat.pleaseSelect")));
				return;
			}
			List<TaskExe> list= taskExeService.cancelBat(idStr, opinion, informType, sysUser);
			//添加日志。
			addRunLong(list);

			String message=MessageUtil.getMessage();
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Success, message));
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Fail, getText("controller.bpmTaskExe.cancelBat.fail")));
		}
	}

	/**
	 * 删除任务转办代理
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除任务转办代理")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			taskExeService.delByIds(lAryId);
			message = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
		} catch (Exception ex) {
			message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail")+":"+ ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 编辑任务转办代理
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description = "编辑任务转办代理")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = RequestUtil.getLong(request, "id");
		String returnUrl = RequestUtil.getPrePage(request);
		TaskExe bpmTaskExe = taskExeService.getById(id);

		return getAutoView().addObject("bpmTaskExe", bpmTaskExe).addObject(
				"returnUrl", returnUrl);
	}

	/**
	 * 取得任务转办代理明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description = "查看任务转办代理明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		TaskExe bpmTaskExe = taskExeService.getById(id);
		return getAutoView().addObject("bpmTaskExe", bpmTaskExe);
	}

	/**
	 * 转办窗口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("assign")
	@Action(description = "转办窗口")
	public ModelAndView assign(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String taskId = RequestUtil.getString(request, "taskId");
		ISysTemplate sysTemplate = sysTemplateService.getDefaultByUseType(ISysTemplate.USE_TYPE_DELEGATE);
		
		return this.getAutoView()
				.addObject("taskId", taskId)
				.addObject("sysTemplate", sysTemplate)
				.addObject("handlersMap",handlersMap);
	}
	
	
	/**
	 * 是否允许转办
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("isAssigneeTask")
	@Action(description = "是否允许转办")
	public void isAssigneeTask(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long taskId = RequestUtil.getLong(request, "taskId");
		ResultMessage result  = null;
		if(!taskExeService.isAssigneeTask(taskId))
			result = new ResultMessage(ResultMessage.Success, getText("controller.bpmTaskExe.isAssigneeTask.allow"));
		else
			result = new ResultMessage(ResultMessage.Fail, getText("controller.bpmTaskExe.isAssigneeTask.notAllow"));
	
		writeResultMessage(response.getWriter(), result);
	}
	
	@RequestMapping("batCancel")
	@Action(description="批量取消")
	public ModelAndView batCancel(HttpServletRequest request) throws Exception
	{
//		String ids=RequestUtil.getString(request,"ids");
//		List<String> paramList=new ArrayList<String>();
//		paramList.add(ids);
		return getAutoView()
//				.addObject("param",paramList)
				.addObject("handlersMap",handlersMap);
				
	}
	
	@RequestMapping("cancelDialog")
	@Action(description="批量取消")
	public ModelAndView cancelDialog(HttpServletRequest request) throws Exception
	{
		return getAutoView()
				.addObject("handlersMap",handlersMap);
				
	}
}
