package com.cssrc.ibms.core.jms.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.web.MessageQuery;
import org.apache.activemq.web.QueueBrowseQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.jms.QueuesService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
/**
 * <pre>
 * 对象功能:ACTIVEMQ_LOCK 控制器类 
 * 开发人员:zhulongchao 
 * </pre>
 */
@Controller
@RequestMapping("/oa/console/queues/")
@Action(ownermodel=SysAuditModelType.MESSAGEMONITOR_ASSIST)
public class QueuesController extends BaseController {
	protected Logger logger = LoggerFactory.getLogger(QueuesController.class);
	@Resource
	private QueuesService queuesService;

	/**
	 * 取得queues分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "查看队列分页列表",detail="查看队列分页列表",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/*如果查询不到队列详细信息，可做如下测试
		1，注释掉 app-jms.xml中的 ，消息监听容器 messageListenerContainer
        2，注释掉 app-jms.xml中的， 邮件消息消费监听器 messageMsgListener
		queuesService.sendMailQueue();//3，取消此行注释 ，测试用
        */
		Collection<Object> list = queuesService.getQueues();//yangbo
		ModelAndView mv = this.getAutoView().addObject("queuesList", list);
		return mv;
	}

	@RequestMapping("browse")
	@SuppressWarnings("unused")
	@Action(description = "查看消息分页列表",detail="查看消息分页列表",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView browse(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Collection<?> list = null;
		List<Message> messageList = new ArrayList<Message>();
		String JMSDestination = RequestUtil.getString(request, "JMSDestination");

		QueueBrowseQuery qbq = queuesService.getQueueBrowseQuery(JMSDestination);
		qbq.setJMSDestinationType("topic");
		javax.jms.QueueBrowser queueBrowser = null;
		queueBrowser = qbq.getBrowser();
		Enumeration iter = queueBrowser.getEnumeration();
		while (iter.hasMoreElements()) {
			javax.jms.Message message = (javax.jms.Message) iter.nextElement();
			logger.info(message.toString());
			messageList.add(message);
		}
		ModelAndView mv = this.getAutoView()
		        .addObject("browseList",messageList)
				.addObject("JMSDestination",JMSDestination);
		return mv;
	}
	
    /**
     *  
     * @version 创建时间：2013-6-8  下午2:37:48
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	@RequestMapping("message")
	@SuppressWarnings("unused")
	@Action(description = "查看message内容分页列表",detail="查看message内容分页列表",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView message(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Message> messageList = new ArrayList<Message>();
		String id = RequestUtil.getString(request, "id");
		String JMSDestination = RequestUtil.getString(request, "JMSDestination");
		MessageQuery messageQuery = queuesService.getMessageQuery(id,JMSDestination);
		Message msg=messageQuery.getMessage();
		ModelAndView mv = this.getAutoView().addObject("messageQuery", messageQuery);
		return mv;
	}
	/**
	 * 删除队列
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除队列",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除队列" +
					"<#list StringUtils.split(lAryId,\",\") as item>" +
					"【${item}】" +
				"</#list>",exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			String[] lAryId = RequestUtil.getStringAryByStr(request, "JMSDestination");
			
			for (String name : lAryId){
				queuesService.removeDestinationQueue(name);
			}
			LogThreadLocalHolder.putParamerter("lAryId", lAryId);
			message = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail") + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
/**
 * 删除消息 
 * @version 创建时间：2013-6-18  下午10:11:42
 * @param request
 * @param response
 * @throws Exception
 */
	@RequestMapping("delMessage")
	@Action(description = "删除消息",execOrder=ActionExecOrder.BEFORE,
			detail="删除消息",exectype = SysAuditExecType.DELETE_TYPE)
	public void delMessage(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			String JMSDestination = RequestUtil.getString(request, "JMSDestination");
			
			String messageId = RequestUtil.getString(request, "messageId");
			queuesService.removeMessage(JMSDestination,messageId);
			
			message = new ResultMessage(ResultMessage.Success,getText("controller.del.success"));
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail")+ ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 清空队列
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("purge")
	@Action(description = "清空队列",detail="清空队列",exectype = SysAuditExecType.UPDATE_TYPE)
	public void purge(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			String[] lAryId = RequestUtil.getStringAryByStr(request, "JMSDestination");
			
			for (String name : lAryId){
				queuesService.purgeDestination(name);
			}
			message = new ResultMessage(ResultMessage.Success,getText("controller.queues.clean.success"));
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, getText("controller.queues.clean.fail")
					+ ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
}
