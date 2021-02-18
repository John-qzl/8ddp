package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang.StringUtils;

import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.api.jms.model.MessageModel;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.flow.model.NodeMessage;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 发送消息任务任务。
 * 
 * @author ray
 * 
 */
public class MessageTask implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		sendMessage(execution);
	}

	private void sendMessage(DelegateExecution execution) throws Exception {
		NodeMessageService bpmNodeMessageService = (NodeMessageService) AppUtil.getBean(NodeMessageService.class);
		ProcessRunService processRunService= (ProcessRunService) AppUtil.getBean(ProcessRunService.class);
		
		String actDefId = execution.getProcessDefinitionId();
		String nodeId = execution.getCurrentActivityId();
		String actInstId = execution.getProcessInstanceId();
		Map<String,Object> variables=execution.getVariables();
		ProcessRun processRun = processRunService.getByActInstanceId(Long.valueOf(actInstId));
		Long runId = processRun.getRunId();
		String processTitle = processRun.getSubject();
		ISysUser currentUser=(ISysUser)UserContextUtil.getCurrentUser(-1L,"系统");
		List<NodeMessage> bpmNodeMessages = bpmNodeMessageService.getNodeMessages(actDefId, nodeId);
		for(NodeMessage message:bpmNodeMessages){
			Short messageType = message.getMessageType();
			if(BeanUtils.isEmpty(message)||message.getIsSend()==0)continue;
			
			if(messageType.equals(NodeMessage.MESSAGE_TYPE_INNER)){
				sendInnerMessage(execution, message, currentUser, processTitle, runId, variables);
			}else if(messageType.equals(NodeMessage.MESSAGE_TYPE_MAIL)){
				sendMailMessage(execution, message, currentUser, processTitle, runId, variables);
			}else if(messageType.equals(NodeMessage.MESSAGE_TYPE_SMS)){
				sendSmsMessage(execution, message.getTemplate(), processTitle, currentUser,variables);
			}
		}
	}

	/**
	 * 发送手机短信
	 * @param execution
	 * @param template
	 * @param title
	 * @param currentUser
	 */
	private void sendSmsMessage(DelegateExecution execution,String template,String title,ISysUser currentUser,Map<String,Object> variables) {
		
		
		IMessageProducer messageProducer = (IMessageProducer) AppUtil.getBean(IMessageProducer.class);
		NodeMessageService bpmNodeMessageService = (NodeMessageService) AppUtil.getBean(NodeMessageService.class);
		List<ISysUser> users=bpmNodeMessageService.getSmsReceiver( execution,currentUser.getUserId());
		if(BeanUtils.isEmpty(users)) return;
		
		for(ISysUser user:users){
			String phoneNumber = user.getMobile();
			if(StringUtil.isEmpty(phoneNumber) || !StringUtil.isMobile(phoneNumber)) continue;
			
			String content = MsgUtil.replaceTemplateTag(template, user.getFullname() , currentUser.getFullname() , title, "", null ,false);
			content=BpmUtil.getTitleByRule(content, variables);
//			SmsMobile smsMobile = new SmsMobile();
//			smsMobile.setPhoneNumber(user.getMobile());
//			smsMobile.setSmsContent(content);
//			messageProducer.send(smsMobile);
			//消息发送模型统一用MessageModel
			MessageModel messageModel=new MessageModel(BpmConst.MESSAGE_TYPE_SMS);
			messageModel.setContent(content);
			messageModel.setReceiveUser(user.getUserId());
			messageModel.setSendUser(currentUser.getUserId());
			messageProducer.send(messageModel);	
		}
	}
	
	/**
	 * 发送邮件
	 * @param execution
	 * @param message
	 * @param currentUser
	 * @param title
	 * @param runId
	 * @param variables
	 */
	private void sendMailMessage(DelegateExecution execution,NodeMessage message,ISysUser currentUser,String title,Long runId,Map<String,Object>variables) {
		IMessageProducer messageProducer = (IMessageProducer) AppUtil.getBean(IMessageProducer.class);
		NodeMessageService bpmNodeMessageService = (NodeMessageService) AppUtil.getBean(NodeMessageService.class);
		List<ISysUser> mailReceiver = bpmNodeMessageService.getMailReceiver( execution,currentUser.getUserId());
		List<ISysUser> mailCopyTo = bpmNodeMessageService.getMailCopyTo( execution,currentUser.getUserId());
		List<ISysUser> mailBcc = bpmNodeMessageService.getMailBcc( execution,currentUser.getUserId());
		List<String> receiverMails = new ArrayList<String>();
		List<String> receiverNames = new ArrayList<String>();
		for(ISysUser user:mailReceiver){
			String mail = user.getEmail();//外网邮箱
			if(StringUtil.isNotEmpty(mail) && StringUtil.isEmail(mail)){
				receiverMails.add(mail);
			}
			receiverNames.add(user.getFullname());
			receiverMails.add(user.getUserId().toString());
		}
		List<String> receiverCopyTos = new ArrayList<String>();
		for(ISysUser user:mailCopyTo){
			String mail = user.getEmail();//外网邮箱
			if(StringUtil.isNotEmpty(mail) && StringUtil.isEmail(mail)){
				receiverCopyTos.add(mail);
			}
			receiverCopyTos.add(user.getUserId().toString());
			
		}
		List<String> receiverBccs = new ArrayList<String>();
		for(ISysUser user:mailBcc){
			String mail = user.getEmail();//外网邮箱
			if(StringUtil.isNotEmpty(mail) && StringUtil.isEmail(mail)){
				receiverBccs.add(mail);
			}
			receiverBccs.add(user.getUserId().toString());
		}
		String url = FlowUtil.getUrl(runId.toString(), false);
		
		String subject = MsgUtil.replaceTitleTag(message.getSubject() , StringUtils.join(receiverNames,",") ,currentUser.getFullname(), title,"");
		String content = MsgUtil.replaceTemplateTag(message.getTemplate(), StringUtils.join(receiverNames,",") , currentUser.getFullname() , title, url, "" ,false);
//		MailModel mailModel = new MailModel();
//		mailModel.setSubject(subject);
//		mailModel.setContent(BpmUtil.getTitleByRule(content, variables));
//		mailModel.setSendDate(new Date());
//		mailModel.setTo(receiverMails.toArray(new String[0]));
//		mailModel.setCc(receiverCopyTos.toArray(new String[0]));
//		mailModel.setBcc(receiverBccs.toArray(new String[0]));
//		messageProducer.send(mailModel);
		//消息发送模型统一用MessageModel
		content=BpmUtil.getTitleByRule(content, variables);
		MessageModel messageModel=new MessageModel(BpmConst.MESSAGE_TYPE_MAIL);
		messageModel.setSubject(subject);
		messageModel.setContent(content);
		messageModel.setSendDate(new Date());
		messageModel.setTo((String[])receiverMails.toArray(new String[0]));
		messageModel.setCc(receiverCopyTos);
		messageModel.setBcc(receiverBccs);
		messageProducer.send(messageModel);	
	}

	/**
	 * 发送站内消息
	 * @param execution
	 * @param message
	 * @param currentUser
	 * @param title
	 * @param runId
	 * @param variables
	 */
	private void sendInnerMessage(DelegateExecution execution,NodeMessage message,ISysUser currentUser,String title,Long runId,Map<String,Object>variables) {
		IMessageProducer messageProducer = (IMessageProducer) AppUtil.getBean(IMessageProducer.class);
		NodeMessageService bpmNodeMessageService = (NodeMessageService) AppUtil.getBean(NodeMessageService.class);
		List<ISysUser> users=bpmNodeMessageService.getInnerReceiver( execution,currentUser.getUserId());
		if(BeanUtils.isEmpty(users)) return;
		
		for(ISysUser user:users){
			String url = FlowUtil.getUrl(runId.toString(), false );
			
			String subject = MsgUtil.replaceTitleTag(message.getSubject() , user.getFullname() ,currentUser.getFullname(), title,"");
			String content = MsgUtil.replaceTemplateTag(message.getTemplate(), user.getFullname() , currentUser.getFullname() , title, url, "" ,false);
//			InnerMessage innerModel = new InnerMessage();
//			innerModel.setContent(BpmUtil.getTitleByRule(content, variables));
//			innerModel.setSendDate(new Date());
//			innerModel.setSubject(subject);
//			innerModel.setFrom(currentUser.getUserId().toString());
//			innerModel.setFromName(currentUser.getFullname());
//			innerModel.setTo(user.getUserId().toString());
//			innerModel.setToName(user.getFullname());
//			messageProducer.send(innerModel);
			content=BpmUtil.getTitleByRule(content, variables);
			//消息发送模型统一用MessageModel
			MessageModel messageModel=new MessageModel(BpmConst.MESSAGE_TYPE_INNER);
			messageModel.setSubject(subject);
			messageModel.setContent(content);
			messageModel.setSendDate(new Date());
			messageModel.setReceiveUser(user.getUserId());
			messageModel.setSendUser(currentUser.getUserId());
			messageProducer.send(messageModel);	
		}
		
		
	}

}
