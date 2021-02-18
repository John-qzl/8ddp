package com.cssrc.ibms.core.msg.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.msg.model.MessageReceiver;
import com.cssrc.ibms.core.msg.model.MessageReply;
import com.cssrc.ibms.core.msg.model.MessageSend;
import com.cssrc.ibms.core.msg.service.MessageReceiverService;
import com.cssrc.ibms.core.msg.service.MessageReplyService;
import com.cssrc.ibms.core.msg.service.MessageSendService;

/**
 * 站内信信息回复表单
 * <p>Title:MessageReplyFormController</p>
 * @author Yangbo 
 * @date 2016年9月28日下午4:00:27
 */
@Controller
@RequestMapping({ "/oa/system/messageReply/" })
public class MessageReplyFormController extends BaseFormController {

	@Resource
	private MessageReplyService messageReplyService;
	@Resource
	private MessageSendService messageSendService;
	@Resource
	private MessageReceiverService messageReceiverService;

	@RequestMapping({ "save" })
	@Action(description = "添加或更新消息回复")
	public void save(HttpServletRequest request, HttpServletResponse response,
			MessageReply messageReply, BindingResult bindResult)
			throws Exception {
		ResultMessage resultMessage = validForm("messageReply", messageReply,
				bindResult, request);

		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		String resultMsg = null;
		Date now = new Date();
		//回复分两步：1.先把消息发给对方(接收者为第一次发送方)
				//2.把记录存在reply中，表示当前用户回复
		if (messageReply.getId() == null) {
			//第一步
			MessageSend msgsend=messageSendService.getById(messageReply.getMessageId());
			MessageReceiver messageReceiver = new MessageReceiver();
			messageReceiver.setId(UniqueIdUtil.genId());
			messageReceiver.setMessageId(messageReply.getMessageId());
			messageReceiver.setReceiverId(msgsend.getUserId());
			messageReceiver.setReceiver(msgsend.getUserName());
			if(msgsend.getMessageType().equals("1")){
				messageReceiver.setReceiveType(new Short("0"));
			}else{
				messageReceiver.setReceiveType(new Short("1"));
			}
			messageReceiverService.add(messageReceiver);
			//第二部
			messageReply.setId(Long.valueOf(UniqueIdUtil.genId()));
			messageReply.setReplyTime(now);
			this.messageReplyService.add(messageReply);
			resultMsg = "添加消息回复成功";
		} else {
			this.messageReplyService.update(messageReply);
			resultMsg = "更新消息回复成功";
		}
		writeResultMessage(response.getWriter(), resultMsg, 1);
	}

	@ModelAttribute
	protected MessageReply getFormObject(@RequestParam("id") Long id,
			Model model) throws Exception {
		this.logger.debug("enter MessageReply getFormObject here....");
		MessageReply messageReply = null;
		if (id != null)
			messageReply = (MessageReply) this.messageReplyService.getById(id);
		else {
			messageReply = new MessageReply();
		}
		return messageReply;
	}
}
