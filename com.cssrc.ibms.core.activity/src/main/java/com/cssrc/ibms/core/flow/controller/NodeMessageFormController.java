package com.cssrc.ibms.core.flow.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.flow.model.NodeMessage;
import com.cssrc.ibms.core.flow.service.NodeMessageService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;

/**
 * 对象功能:流程消息节点 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/nodeMessage/")
public class NodeMessageFormController extends BaseFormController
{	
	@Resource
	private NodeMessageService nodeMessageService;
	/**
	 * 添加或更新流程节点邮件。
	 * @param request
	 * @param response
	 * @param bpmNodeMessage 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新流程节点邮件")
	public void save(HttpServletRequest request, HttpServletResponse response, NodeMessage bpmNodeMessage,BindingResult bindResult) throws Exception
	{		
		String actDefId=RequestUtil.getString(request,"actDefId");
		String nodeId=RequestUtil.getString(request,"nodeId");
		String subject_mail=RequestUtil.getString(request,"subject_mail");
		String template_mail=RequestUtil.getString(request,"template_mail");
		
		String subject_inner=RequestUtil.getString(request,"subject_inner");
		String template_inner=RequestUtil.getString(request,"template_inner");

		String template_mobile=RequestUtil.getString(request,"template_mobile");
		
		Short sendMail=RequestUtil.getShort(request, "sendMail");
		Short sendInner=RequestUtil.getShort(request, "sendInner");
		Short sendMobile=RequestUtil.getShort(request, "sendMobile");
		Long mailMessageId = RequestUtil.getLong(request, "mailMessageId");
		Long innerMessageId = RequestUtil.getLong(request, "innerMessageId");
		Long smsMessageId = RequestUtil.getLong(request, "smsMessageId");
		
		List<NodeMessage> messages=new ArrayList<NodeMessage>();
		
		NodeMessage mailMessage=new NodeMessage();
		mailMessage.setId(mailMessageId);
		mailMessage.setActDefId(actDefId);
		mailMessage.setNodeId(nodeId);
		mailMessage.setSubject(subject_mail);
		mailMessage.setMessageType(NodeMessage.MESSAGE_TYPE_MAIL);
		mailMessage.setTemplate(template_mail);
		mailMessage.setIsSend(sendMail);
		
		NodeMessage innerMessage=new NodeMessage();
		innerMessage.setId(innerMessageId);
		innerMessage.setActDefId(actDefId);
		innerMessage.setNodeId(nodeId);
		innerMessage.setSubject(subject_inner);
		innerMessage.setMessageType(NodeMessage.MESSAGE_TYPE_INNER);
		innerMessage.setTemplate(template_inner);
		innerMessage.setIsSend(sendInner);
		
		NodeMessage smsMessage=new NodeMessage();
		smsMessage.setId(smsMessageId);
		smsMessage.setActDefId(actDefId);
		smsMessage.setNodeId(nodeId);
		smsMessage.setMessageType(NodeMessage.MESSAGE_TYPE_SMS);
		smsMessage.setTemplate(template_mobile);
		smsMessage.setIsSend(sendMobile);
		
		messages.add(mailMessage);
		messages.add(innerMessage);
		messages.add(smsMessage);
		
		for(NodeMessage message:messages){
			if(message.getId().intValue()==0){
				message.setId(UniqueIdUtil.genId());
				nodeMessageService.add(message);
			}else{
				nodeMessageService.update(message);
			}
		}
		writeResultMessage(response.getWriter(),getText("service.bpmNodeMessage.save.success"),ResultMessage.Success);
	}
	
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected NodeMessage getFormObject(@RequestParam("id") Long id,Model model) throws Exception {
		logger.debug("enter NodeMessage getFormObject here....");
		NodeMessage bpmNodeMessage=null;
		if(id!=null){
			bpmNodeMessage=nodeMessageService.getById(id);
		}else{
			bpmNodeMessage= new NodeMessage();
		}
		return bpmNodeMessage;
    }

}
