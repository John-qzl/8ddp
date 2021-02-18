package com.cssrc.ibms.core.msg.controller;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.msg.model.MessageReceiver;
import com.cssrc.ibms.core.msg.service.MessageReceiverService;
import com.cssrc.ibms.core.msg.service.MessageSendService;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
/**
 * 消息Controller层
 * @author Yangbo 2016-7-23
 *
 */
@Controller
@RequestMapping({"/oa/system/messageReceiver/"})
@Action(ownermodel=SysAuditModelType.USER_MANAGEMENT)
public class MessageReceiverController extends BaseController
{

	@Resource
	private MessageReceiverService receiverService;

	@Resource
	private MessageSendService sendService;

	@RequestMapping({"list"})
	@Action(description="查看消息接收者分页列表", detail="查看消息接收者分页列表")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		QueryFilter queryFilter = new QueryFilter(request, "messageReceiverItem");
		queryFilter.addFilterForIB("receiverId", UserContextUtil.getCurrentUserId());
		List list = this.sendService.getReceiverByUser(queryFilter);
		ModelAndView mv = getAutoView().addObject("messageReceiverList", list).addObject("pam", queryFilter.getFilters());

		return mv;
	}

	@RequestMapping({"del"})
	@Action(description="删除消息接收者", execOrder=ActionExecOrder.BEFORE, detail="删除消息接收者<#list StringUtils.split(id,\",\") as item><#assign entity=messageReceiverService.getById(Long.valueOf(item))/>【${entity.receiver}】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.receiverService.delByIdsCascade(lAryId);
			message = new ResultMessage(1, "删除消息接收者成功!");
		}
		catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({"edit"})
	@Action(description="添加或编辑消息接收者", detail="<#if isAdd>添加消息接收者<#else>编辑消息接收者<#assign entity=messageReceiverService.getById(Long.valueOf(id))/>【${entity.receiver}】</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String returnUrl = RequestUtil.getPrePage(request);
		MessageReceiver messageReceiver = null;
		boolean isadd = true;
		if (id.longValue() != 0L) {
			messageReceiver = (MessageReceiver)this.receiverService.getById(id);
			isadd = false;
		} else {
			messageReceiver = new MessageReceiver();
		}
		LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		return getAutoView().addObject("messageReceiver", messageReceiver).addObject("returnUrl", returnUrl);
	}

	@RequestMapping({"get"})
	@Action(description="查看消息接收者明细", detail="查看消息接收者明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		long id = RequestUtil.getLong(request, "id");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		MessageReceiver messageReceiver = (MessageReceiver)this.receiverService.getById(Long.valueOf(id));
		return getAutoView().addObject("messageReceiver", messageReceiver).addObject("canReturn", Long.valueOf(canReturn));
	}

	@RequestMapping({"mark"})
	@ResponseBody
	@Action(description="标记消息为已读", detail="标记消息为已读")
	public String mark(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long[] lAryId = RequestUtil.getLongAryByStr(request, "ids");
		JSONObject jobject = new JSONObject();
		try {
			this.receiverService.updateReadStatus(lAryId);
			jobject.accumulate("result", true)
			.accumulate("message", "成功标记为已读!");
		}
		catch (Exception ex) {
			jobject.accumulate("result", false)
			.accumulate("message", "删除失败:" + ex.getMessage());
		}

		return jobject.toString();
	}
	
	@RequestMapping({"getMsgCount"})
	@ResponseBody
	@Action(description="获取未读消息数量", detail="获取未读消息数量")
	public void getMsgCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		try {
			Long userId = UserContextUtil.getCurrentUserId();
			//未读消息
			Integer count = this.sendService.getCountReceiverByUser(userId);
			String msgCount = "";
			if(count!=null){
				msgCount = count.toString();
			}

			ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, msgCount);
			out.print(resultMessage);
		} catch (Exception e) {
			// TODO: handle exception
			ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, e.getMessage());
			out.print(resultMessage);
		}
	}
	
	@RequestMapping({"delMsg"})
	@ResponseBody
	@Action(description="删除消息", detail="删除消息")
	public void delMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "ids");
			this.receiverService.delByIdsCascade(lAryId);
			message = new ResultMessage(1, "删除消息成功!");
			out.print(message);
		} catch (Exception e) {
			// TODO: handle exception
			message = new ResultMessage(0, "删除消息失败:" + e.getMessage());
			out.print(message);
		}
	}
}

