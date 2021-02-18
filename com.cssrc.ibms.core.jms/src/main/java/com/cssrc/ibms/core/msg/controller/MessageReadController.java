package com.cssrc.ibms.core.msg.controller;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.msg.model.MessageRead;
import com.cssrc.ibms.core.msg.model.MessageSend;
import com.cssrc.ibms.core.msg.service.MessageReadService;
import com.cssrc.ibms.core.msg.service.MessageReplyService;
import com.cssrc.ibms.core.msg.service.MessageSendService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( { "/oa/system/messageRead/" })
public class MessageReadController extends BaseController {

	@Resource
	private MessageReadService readService;

	@Resource
	private MessageSendService sendService;

	@Resource
	private MessageReplyService replyService;

	@RequestMapping( { "list" })
	@Action(description = "查看接收状态分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Long userId = curUser.getUserId();
		String returnUrl = RequestUtil.getPrePage(request);
		Long messageId = Long
				.valueOf(RequestUtil.getLong(request, "messageId"));

		MessageSend messageSend = new MessageSend();
		messageSend = (MessageSend) this.sendService.getById(messageId);
		QueryFilter queryFilter = new QueryFilter(request, "messageReplyItem",
				false);
		queryFilter.addFilterForIB("userId", userId);
		List list = this.replyService.getAll(queryFilter);

		this.readService.addMessageRead(messageId, curUser);

		ModelAndView mv = getAutoView().addObject("replyList",
				list.size() > 0 ? list : null).addObject("userId", userId)
				.addObject("messageSend", messageSend).addObject("returnUrl",
						returnUrl);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除接收状态")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.readService.delByIds(lAryId);
			message = new ResultMessage(1, "删除接收状态成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑接收状态")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String returnUrl = RequestUtil.getPrePage(request);
		MessageRead messageRead = null;
		if (id.longValue() != 0L)
			messageRead = (MessageRead) this.readService.getById(id);
		else {
			messageRead = new MessageRead();
		}
		return getAutoView().addObject("messageRead", messageRead).addObject(
				"returnUrl", returnUrl);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看接收状态明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		MessageRead messageRead = (MessageRead) this.readService.getById(Long
				.valueOf(id));
		return getAutoView().addObject("messageRead", messageRead);
	}
}
