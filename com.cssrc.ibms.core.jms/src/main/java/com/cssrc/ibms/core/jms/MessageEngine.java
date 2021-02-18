package com.cssrc.ibms.core.jms;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

import com.cssrc.ibms.api.system.intf.IMessageLogService;
import com.cssrc.ibms.api.system.intf.IMessageSendService;
import com.cssrc.ibms.api.system.model.IMessageLog;
import com.cssrc.ibms.api.system.model.IMessageSend;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.mail.MailUtil;
import com.cssrc.ibms.core.mail.model.Mail;
import com.cssrc.ibms.core.mail.model.MailSeting;
import com.cssrc.ibms.core.util.appconf.AppUtil;

import rtx.RTXSvrApi;


 

/**
 * 消息引擎
 * 
 * @author zhulongchao
 * 
 */
public class MessageEngine {
	private final Logger logger = Logger.getLogger(MessageEngine.class);
	private MailUtil mailUtil;
	public void setMailUtil(MailUtil mailSender)
	{
		this.mailUtil = mailSender;
	}

	/**
	 * 发送邮件
	 * 
	 * @param msg
	 *            邮件对象
	 * @param ifHtml
	 *            是否为html内容
	 */
	public void sendMail(Mail mail) {
		Integer state = IMessageLog.STATE_SUCCESS;
		try {
			MailSeting mailSetting = (MailSeting)AppUtil.getBean(MailSeting.class);

			mail.setSenderAddress(mailSetting.getMailAddress());
			mail.setSenderName(mailSetting.getNickName());
			mail.setSendDate(new Date());
			this.mailUtil.send(mail);
		} catch (Exception ex) {
		    logger.error(ex);
			state = IMessageLog.STATE_FAIL;
		}
		// 保存发送消息日志
		IMessageLogService messageLogService = 
				(IMessageLogService)AppUtil.getBean(IMessageLogService.class);
		messageLogService
		.addMessageLog(mail.getSubject(), mail.getReceiverAddresses(), 
				IMessageLog.MAIL_TYPE, state);
	}

	 
	/**
	 * 发送内部消息
	 * 
	 * @param messageSend
	 */
	public void sendInnerMessage(IMessageSend messageSend) {
		Integer state = IMessageLog.STATE_SUCCESS;
		try {
			IMessageSendService messageSendService = (IMessageSendService) AppUtil
					.getBean(IMessageSendService.class);
			messageSendService.addMessageSend(messageSend);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			state = IMessageLog.STATE_FAIL;
		}
		// 保存发送消息日志
		IMessageLogService messageLogService = (IMessageLogService) AppUtil
				.getBean(IMessageLogService.class);
		messageLogService.addMessageLog(messageSend.getSubject(),
				messageSend.getReceiverName(), IMessageLog.INNER_TYPE, state);
	}
	/**
	 * 发送RTx即时通讯
	 * 
	 * @param messageSend
	 */
	public void sendRtxMessage(IMessageSend messageSend) {
		//取得接收人
		String receiverName =  messageSend.getReceiverName();
		//腾讯通消息提醒开启情况下可以发送消息，否则不发送消息提醒，并且判断接收人是否存在
		if(SysConfConstant.RTX_NOTIFY_ON_OFF ==1&&receiverName!=null&&!receiverName.equals("")){
			Integer state = IMessageLog.STATE_SUCCESS;
			try {
				//RTX接口类
				RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();    
				//初始化
				if( RtxsvrapiObj.Init()){
					//登录审批界面
					//String parameterUrl = "";
					String contentText = "[点击进入"+SysConfConstant.SYSTEM_TITLE+"|"+SysConfConstant.RTX_NOTIFY_LINK+"]\n";
					int iRet = RtxsvrapiObj.sendNotify(receiverName,messageSend.getSubject(), contentText+messageSend.getContent(), "0","0");
		         	if (iRet == 0){
		         		state = IMessageLog.STATE_SUCCESS;
		    		}else {
		    			state = IMessageLog.STATE_FAIL;
		    		}
				}
				RtxsvrapiObj.UnInit();
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				state = IMessageLog.STATE_FAIL;
			}
			// 保存发送消息日志
			IMessageLogService messageLogService = (IMessageLogService) AppUtil
					.getBean(IMessageLogService.class);
			messageLogService.addMessageLog(messageSend.getSubject(),
					messageSend.getReceiverName(), IMessageLog.RTX_TYPE, state);
		}
	}

}
