package com.cssrc.ibms.homePage.controller;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.activity.model.IProcessTask;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.msg.model.MessageSend;
import com.cssrc.ibms.core.msg.service.MessageSendService;
import com.cssrc.ibms.core.util.annotion.Action;

import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceMessageDao;
import com.cssrc.ibms.dp.product.acceptance.dao.WorkBoardDao;
import com.cssrc.ibms.dp.system.dao.HomeMessageDao;


import com.cssrc.ibms.system.service.IndexShowService;
import com.fr.web.core.A.w;

import org.apache.activemq.store.kahadb.disk.page.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>和首页工作台定制相关的控制器类</p>
 * @author majing
 *
 */
@Controller
@RequestMapping("/oa/home/")
public class HomePageController extends BaseController{
	
	@Resource
	private IndexShowService indexShowService;
	
	@Resource
	private IBpmService bpmService;
	
	@Resource
	private IProcessRunService processRunService;
	
	@Resource
	private MessageSendService sendService;
	@Resource
	private AcceptanceMessageDao acceptanceMessageDao;
	@Resource
	private HomeMessageDao homeMessageDao;
	@Resource
	private WorkBoardDao WorkBoardDao;
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("show")
	@Action(description = "显示首页工作台")
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mv = getAutoView();
		
		Long userId = UserContextUtil.getCurrentUserId();
		//Long orgId = UserContextUtil.getCurrentOrgId();
		
		PagingBean page = new PagingBean();
		page.setPageSize(-1);
		//page.setCurrentPage(Integer.valueOf(0));
		//page.setShowTotal(true);
		
		
		List<Map<String, Object>> acceptanceMessageList=acceptanceMessageDao.getUserMessage();
		List<Map<String, Object>> systemMessageList=homeMessageDao.getSystemMessage();
		List<? extends IProcessTask> pendingMatters = bpmService.getTasks(userId,
				null, null, null, null, "desc", page);
		int workBoardNumber=WorkBoardDao.getByCurrentNumber();
		/*InsPortalParams params = new InsPortalParams();
		params.setUserId(userId.toString());
		params.setOrgId(orgId.toString());
		params.setPage(0);
		params.setPageSize(4);*/
		/*List<? extends IProcessTask> pendingMatters = indexShowService.pendingMatters(params, userId);*/
		mv.addObject("tasklist", pendingMatters);
		mv.addObject("dbrw", pendingMatters.size());
		mv.addObject("accMessage",acceptanceMessageList);
		QueryFilter queryFilterRequest=new QueryFilter();
		queryFilterRequest.addFilterForIB("creatorId", userId);
		queryFilterRequest.addFilterForIB("isStart", 1);
		List<?extends IProcessRun> myStartList = processRunService.getMyRequestList(queryFilterRequest);
		List<?extends IProcessRun> myAlreadyList = processRunService.myAlready(userId, null);
		List<?extends IProcessRun> completedMatters = processRunService.completedMatters(userId, null);
		List<?extends IProcessRun> myDraft = processRunService.getMyDraft(userId, null);
		
		mv.addObject("qqlist", myStartList);
		mv.addObject("yblist", myAlreadyList);
		mv.addObject("cglist", myDraft);
		mv.addObject("wdqq", myStartList.size());
		mv.addObject("ybrw", myAlreadyList.size());
		mv.addObject("wdcg", myDraft.size());
		mv.addObject("workNumber",workBoardNumber);
		mv.addObject("systemMessage", systemMessageList);
		QueryFilter queryFilter = new QueryFilter(request, false);
		queryFilter.addFilterForIB("receiverId", userId);
		//queryFilter.addFilterForIB("hasRead", 0);
		List<MessageSend> list = sendService.getReceiverByUser(queryFilter);
		mv.addObject("noticelist", list);
		
		return mv;
	}
	@RequestMapping("setMessageRead")
	@Action(description = "首页消息已读")
	@ResponseBody
	public List<Map<String, Object>> setMessageRead(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id=request.getParameter("businesskey");
		String check=request.getParameter("check");
		acceptanceMessageDao.setMessageStatus(id);
		return acceptanceMessageDao.getUserMessageByRead(check);
	}
	
	
	@RequestMapping("getMyRequest")
	@Action(description = "获取我的请求已结束和正在审批中的")
	@ResponseBody
	public List<?extends IProcessRun> getMyRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		QueryFilter queryFilterRequest=new QueryFilter();
		Long userId = UserContextUtil.getCurrentUserId();
		String isStart=request.getParameter("isStart");
		queryFilterRequest.addFilterForIB("creatorId",userId);
		queryFilterRequest.addFilterForIB("isStart", isStart);
		List<?extends IProcessRun> myStartList = processRunService.getMyRequestList(queryFilterRequest);
		return myStartList;
	}
	@RequestMapping("getMyTask")
	@Action(description = "获取我的待办任务")
	@ResponseBody
	public List<?extends IProcessTask> getMyTask(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long userId = UserContextUtil.getCurrentUserId();
		PagingBean page = new PagingBean();
		page.setPageSize(-1);
		List<? extends IProcessTask> pendingMatters = bpmService.getTasks(userId,
				null, null, null, null, "desc", page);
		return pendingMatters;
	}
	@RequestMapping("getMyCompleteTask")
	@Action(description = "获取我的已办任务")
	@ResponseBody
	public List<?extends IProcessRun> getMyCompleteTask(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long userId = UserContextUtil.getCurrentUserId();
		List<?extends IProcessRun> myAlreadyList = processRunService.myAlready(userId, null);
		return myAlreadyList;
	}
	@RequestMapping("getMessage")
	@Action(description = "获取已读未读消息")
	@ResponseBody
	public List<Map<String, Object>> getMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String check=request.getParameter("check");
		return acceptanceMessageDao.getUserMessageByRead(check);
	}
}
