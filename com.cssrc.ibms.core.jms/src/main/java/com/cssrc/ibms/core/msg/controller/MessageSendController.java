package com.cssrc.ibms.core.msg.controller;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.msg.model.MessageReply;
import com.cssrc.ibms.core.msg.model.MessageSend;
import com.cssrc.ibms.core.msg.service.MessageReadService;
import com.cssrc.ibms.core.msg.service.MessageReplyService;
import com.cssrc.ibms.core.msg.service.MessageSendService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( { "/oa/system/messageSend/" })
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
public class MessageSendController extends BaseController {

	@Resource
	private MessageSendService sendService;

	@Resource
	private MessageReadService readService;

	@Resource
	private MessageReplyService replyService;

	@RequestMapping( { "form" })
	@Action(description = "发送和接收列表框架", detail = "发送和接收列表框架")
	public ModelAndView form(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Long userId = curUser.getUserId();
		ModelAndView mv = getAutoView().addObject("userId", userId);
		return mv;
	}

	@RequestMapping( { "list" })
	@Action(description = "查看发送消息分页列表", detail = "查看发送消息分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long userId = UserContextUtil.getCurrentUserId();
		Date now = new Date();
		Long longTime = Long.valueOf(now.getTime());
		int spanTime = PropertyUtil.getIntByAlias("send.timeout",
				Integer.valueOf(200000)).intValue();
		QueryFilter queryFilter = new QueryFilter(request, "messageSendItem");
		queryFilter.addFilterForIB("userId", userId);
		List list = this.sendService.getAll(queryFilter);
		ModelAndView mv = getAutoView().addObject("messageSendList", list)
				.addObject("longTime", longTime).addObject("spanTime",
						Integer.valueOf(spanTime));

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除发送消息", execOrder = ActionExecOrder.BEFORE, detail = "删除发送消息<#list StringUtils.split(id,\",\") as item><#assign entity=messageSendService.getById(Long.valueOf(item))/>【${entity.subject}】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sendService.delByIds(lAryId);
			message = new ResultMessage(1, "删除发送消息成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑发送消息", detail = "<#if isAdd>添加发送消息<#else>编辑发送消息<#assign entity=messageSendService.getById(Long.valueOf(id))/>【${entity.subject}】</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Long userId = curUser.getUserId();
		String returnUrl = RequestUtil.getPrePage(request);
		MessageSend messageSend = null;
		boolean isadd = true;
		if (id.longValue() != 0L) {
			messageSend = (MessageSend) this.sendService.getById(id);
			isadd = false;
		} else {
			messageSend = new MessageSend();
		}
		LogThreadLocalHolder
				.putParamerter("isAdd", Boolean.valueOf(isadd));
		return getAutoView().addObject("messageSend", messageSend).addObject(
				"userId", userId).addObject("returnUrl", returnUrl);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看发送消息明细", detail = "查看发送消息明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		String flag = RequestUtil.getString(request, "flag");
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Long userId = curUser.getUserId();
		MessageSend messageSend = (MessageSend) this.sendService.getById(Long
				.valueOf(id));
		this.readService.addMessageRead(messageSend.getId(), curUser);

		return getAutoView().addObject("messageSend", messageSend).addObject(
				"flag", flag).addObject("userId", userId).addObject(
				"canReturn", Long.valueOf(canReturn));
	}

	@RequestMapping( { "readMsgDialog" })
	@Action(description = "查看未读信息", detail = "查看未读信息")
	public ModelAndView readMsgDialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		ISysUser sysUser = (ISysUser) UserContextUtil.getCurrentUser();
		int size = 0;
		MessageSend messageSend = null;
		if (id != 0L) {
			messageSend = (MessageSend) this.sendService.getById(Long
					.valueOf(id));
		} else {
			List list = this.sendService.getNotReadMsg(sysUser.getUserId());
			if (list.size() > 0)
				messageSend = (MessageSend) list.get(0);
			size = list.size();
		}

		MessageReply msgReply = new MessageReply();
		if (messageSend != null) {
			this.readService.addMessageRead(messageSend.getId(), sysUser);

			msgReply.setMessageId(messageSend.getId());
			msgReply.setIsPrivate(new Short("1"));
		} else {
			messageSend = new MessageSend();
			messageSend.setContent("<span style='color:red'>暂无内部消息。</span>");
		}
		return getAutoView().addObject("messageSend", messageSend).addObject(
				"flag", Boolean.valueOf(size > 1)).addObject("msgReply",
				msgReply);
	}

	@RequestMapping( { "notReadMsg" })
	@Action(description = "未读信息条数", detail = "未读信息条数")
	public void notReadMsg(HttpServletResponse response) throws IOException {
		List list = this.sendService.getNotReadMsg(UserContextUtil
				.getCurrentUserId());

		response.getWriter().print(list.size());
	}

	@RequestMapping( { "readDetail" })
	@Action(description = "查看已读明细", detail = "查看已读明细")
	public ModelAndView readDetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		int conReply = RequestUtil.getInt(request, "canReply");

		List readlist = this.readService.getReadByMsgId(Long.valueOf(id));
		return getAutoView().addObject("readlist", readlist).addObject(
				"canReply", Integer.valueOf(conReply));
	}

	@RequestMapping( { "replyDetail" })
	@Action(description = "查看回复明细", detail = "查看回复明细")
	public ModelAndView replyDetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");

		List replylist = this.replyService.getReplyByMsgId(Long.valueOf(id));
		return getAutoView().addObject("replylist", replylist);
	}
}
