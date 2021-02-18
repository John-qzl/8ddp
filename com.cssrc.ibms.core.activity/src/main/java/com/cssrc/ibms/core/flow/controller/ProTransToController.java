package com.cssrc.ibms.core.flow.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.jms.intf.IMessageHandler;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.ProTransTo;
import com.cssrc.ibms.core.flow.model.ProTransToAssignee;
import com.cssrc.ibms.core.flow.service.ProTransToService;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;

@Controller
@RequestMapping({ "/oa/flow/proTransTo/" })
@Action(ownermodel = SysAuditModelType.FLOW_MANAGEMENT)
public class ProTransToController extends BaseController {

	@Resource
	private ProTransToService proTransToService;

	@Resource
	Map<String, IMessageHandler> handlersMap;

	@Resource
	private ProcessRunService processRunService;

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("matters")
	public ModelAndView dialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = getAutoView();
		return result;
	}

	@RequestMapping({ "mattersList" })
	@Action(description = "流转事宜列表")
	public ModelAndView mattersList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request, "bpmProTransToItem");
		String nodePath = RequestUtil.getString(request, "nodePath");
		if (StringUtils.isNotEmpty(nodePath))
			filter.getFilters().put("nodePath", nodePath + "%");
		filter.addFilterForIB("exceptDefStatus", Integer.valueOf(3));
		Long userId = UserContextUtil.getCurrentUserId();
		filter.addFilterForIB("createUserId", userId);
		List<ProTransTo> list = proTransToService.mattersList(filter);
		ModelAndView mv = getAutoView().addObject("bpmProTransToList", list)
				.addObject("curUserId", userId);

		return mv;
	}

	@RequestMapping({ "showAssignee" })
	@Action(description = "查看流转人")
	public ModelAndView showAssignee(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String taskId = RequestUtil.getString(request, "taskId");
		ProTransTo proTransTo = this.proTransToService.getByTaskId(new Long(
				taskId));
		if (BeanUtils.isEmpty(proTransTo)) {
			return RequestUtil.getTipInfo("流转过程已结束,毋须再添加流转人!");
		}
		List<ProTransToAssignee> list = proTransToService.getAssignee(taskId,
				proTransTo.getAssignee());
		ModelAndView mv = getAutoView().addObject("bpmProTransToAssigneeList",
				list);
		return mv;
	}

	@RequestMapping({ "add" })
	@Action(description = "添加流转人")
	@ResponseBody
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = null;
		String userIds = request.getParameter("cmpIds");
		try {
			Long taskId = Long.valueOf(RequestUtil.getLong(request, "taskId"));
			String opinion = request.getParameter("opinion");
			String informType = RequestUtil.getString(request, "informType");
			ProTransTo bpmProTransTo = this.proTransToService
					.getByTaskId(taskId);
			if (BeanUtils.isEmpty(bpmProTransTo)) {
				resultMessage = new ResultMessage(0, "流转过程已结束,毋须再添加流转人!");
			} else {
				this.proTransToService.addTransTo(bpmProTransTo,
						taskId.toString(), opinion, userIds, informType);
				resultMessage = new ResultMessage(1, "添加流转人成功!");
			}
		} catch (Exception e) {
			resultMessage = new ResultMessage(0, "添加流转人失败!");
			e.printStackTrace();
		}
		out.print(resultMessage);
	}

	@RequestMapping({ "addDialog" })
	@Action(description = "批量取消")
	public ModelAndView addDialog(HttpServletRequest request) throws Exception {
		Long taskId = Long.valueOf(RequestUtil.getLong(request, "taskId"));
		ProTransTo bpmProTransTo = this.proTransToService.getByTaskId(taskId);
		if (BeanUtils.isEmpty(bpmProTransTo)) {
			return RequestUtil.getTipInfo("流转过程已结束,不能继续添加了!");
		}
		return getAutoView().addObject("handlersMap", this.handlersMap)
				.addObject("bpmProTransTo", bpmProTransTo);
	}

	@RequestMapping({ "cancel" })
	@Action(description = "取消流转任务")
	@ResponseBody
	public void cancel(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = null;
		String taskId = RequestUtil.getString(request, "taskId");
		String opinion = RequestUtil.getString(request, "opinion");
		String userId = RequestUtil.getString(request, "userId");
		String informType = RequestUtil.getString(request, "informType");
		try {
			ProcessTask processTask = processRunService
					.getTaskByParentIdAndUser(taskId, userId);
			if (processTask == null) {
				resultMessage = new ResultMessage(0, "此流转任务已被审批!");
			} else {
				this.proTransToService.cancel(processTask, opinion, informType);
				resultMessage = new ResultMessage(1, "取消流转任务成功!");
			}
		} catch (Exception e) {
			resultMessage = new ResultMessage(0, "取消流转任务失败!");
			e.printStackTrace();
		}
		out.print(resultMessage);
	}

	@RequestMapping({ "getByAssignee" })
	@Action(description = "获取流转人及任务情况")
	@ResponseBody
	public List<ProTransToAssignee> getByAssignee(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String parentTaskId = RequestUtil.getString(request, "parentTaskId");
		String assignee = RequestUtil.getString(request, "assignee");
		return this.proTransToService.getAssignee(parentTaskId, assignee);
	}

	@RequestMapping({ "cancelDialog" })
	@Action(description = "批量取消")
	public ModelAndView cancelDialog(HttpServletRequest request)
			throws Exception {
		return getAutoView().addObject("handlersMap", this.handlersMap);
	}
}
