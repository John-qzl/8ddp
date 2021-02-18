package com.cssrc.ibms.core.msg.controller;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.msg.model.MessageReply;
import com.cssrc.ibms.core.msg.model.MessageSend;
import com.cssrc.ibms.core.msg.service.MessageReplyService;
import com.cssrc.ibms.core.msg.service.MessageSendService;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( { "/oa/system/messageReply/" })
public class MessageReplyController extends BaseController {

	@Resource
	private MessageReplyService replyService;

	@Resource
	private MessageSendService sendService;

	@Resource
	private ISysUserService userService;

	@RequestMapping( { "list" })
	@Action(description = "查看消息回复分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Long userId = curUser.getUserId();
		Long messageId = Long
				.valueOf(RequestUtil.getLong(request, "messageId"));
		MessageSend messageSend = (MessageSend) this.sendService
				.getById(messageId);
		QueryFilter queryFilter = new QueryFilter(request, "messageReplyItem",
				false);
		queryFilter.addFilterForIB("userId", userId);
		List list = this.replyService.getAll(queryFilter);
		ModelAndView mv = getAutoView().addObject("replyList", list).addObject(
				"userId", userId).addObject("messageSend", messageSend)
				.addObject("returnUrl", returnUrl);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除消息回复")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.replyService.delByIds(lAryId);
			message = new ResultMessage(1, "删除消息回复成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑消息回复")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		Long messageId = Long
				.valueOf(RequestUtil.getLong(request, "messageId"));
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Long userId = curUser.getUserId();
		String returnUrl = RequestUtil.getPrePage(request);
		MessageReply messageReply = null;
		MessageSend messageSend = null;

		messageSend = (MessageSend) this.sendService.getById(messageId);

		if (id.longValue() != 0L) {
			messageReply = (MessageReply) this.replyService.getById(id);
		} else {
			messageReply = new MessageReply();
			messageReply.setMessageId(messageId);
			messageReply.setReply(curUser.getFullname());
			messageReply.setReplyId(userId);
		}
		return getAutoView().addObject("messageReply", messageReply).addObject(
				"subject", messageSend.getSubject()).addObject("content",
				messageSend.getContent()).addObject("returnUrl", returnUrl);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看消息回复明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		MessageReply messageReply = (MessageReply) this.replyService
				.getById(Long.valueOf(id));
		return getAutoView().addObject("messageReply", messageReply);
	}

	@RequestMapping( { "reply" })
	@Action(description = "读取消息并回复")
	public void reply(HttpServletRequest request, HttpServletResponse response,
			MessageReply messageReply) throws Exception {
		try {
			ISysUser sysUser = (ISysUser) UserContextUtil.getCurrentUser();
			this.replyService.saveReply(messageReply, sysUser);
			writeResultMessage(response.getWriter(), "回复消息成功!", 1);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(0, "回复消息失败:"
						+ str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
}
