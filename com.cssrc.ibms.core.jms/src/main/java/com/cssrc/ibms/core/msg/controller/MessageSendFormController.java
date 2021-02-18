package com.cssrc.ibms.core.msg.controller;

import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.engine.IScript;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.msg.model.MessageSend;
import com.cssrc.ibms.core.msg.service.MessageSendService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({ "/oa/system/messageSend/" })
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
public class MessageSendFormController extends BaseFormController {

	@Resource
	private MessageSendService messageSendService;
	/**
	 * 发送消息
	 * @param request
	 * @param response
	 * @param messageSend
	 * @param bindResult
	 * @throws Exception
	 */
	@RequestMapping({ "save" })
	@Action(description = "添加或更新发送消息", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>发送消息【${SysAuditLinkService.getMessageSendLink(Long.valueOf(mesendId))}】")
	public void save(HttpServletRequest request, HttpServletResponse response,
			MessageSend messageSend, BindingResult bindResult) throws Exception {
		ResultMessage resultMessage = validForm("messageSend", messageSend,	
				bindResult, request);

		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		String receiverId = RequestUtil.getString(request, "receiverId");
		String receiverName = RequestUtil.getString(request, "receiverName");
		String receiverOrgId = RequestUtil.getString(request, "receiverOrgId");
		String receiverOrgName = RequestUtil.getString(request,
				"receiverOrgName");

		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();

		String resultMsg = null;
		boolean isadd = true;
		if (messageSend.getId() == null) {
			resultMsg = "添加发送消息成功";
		} else {
			resultMsg = "更新发送消息成功";
			isadd = false;
		}
		this.messageSendService.addMessageSend(messageSend, curUser,
				receiverId, receiverName, receiverOrgId, receiverOrgName);
		LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		LogThreadLocalHolder.putParamerter("mesendId", messageSend.getId()
				.toString());
		writeResultMessage(response.getWriter(), resultMsg, 1);
	}

	@ModelAttribute
	protected MessageSend getFormObject(@RequestParam("id") Long id, Model model)
			throws Exception {
		this.logger.debug("enter MessageSend getFormObject here....");
		MessageSend messageSend = null;
		if (id != null)
			messageSend = (MessageSend) this.messageSendService.getById(id);
		else {
			messageSend = new MessageSend();
		}
		return messageSend;
	}
}
