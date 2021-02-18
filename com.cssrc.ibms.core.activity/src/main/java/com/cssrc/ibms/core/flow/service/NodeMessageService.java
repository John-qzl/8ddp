package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.NodeMessageDao;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeMessage;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.UserCondition;


/**
 * 对象功能:流程节点消息 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class NodeMessageService extends BaseService<NodeMessage>{
	@Resource
	private NodeMessageDao dao;
		
	@Resource 
	private DefinitionService definitionService;
	
	@Resource
	private NodeSetService nodeSetService;
	
	@Resource
	private UserConditionService userConditionService;
	
	@Resource
	private NodeUserService nodeUserService;
	
	public NodeMessageService()
	{
	}
	
	/**
	 * 根据ACT流程定义id获取流程定义。
	 * @param actDefId
	 * @return
	 */
	public List<NodeMessage> getByActDefId(String actDefId)
	{
		return dao.getByActDefId(actDefId);
	}
	
	@Override
	protected IEntityDao<NodeMessage, Long> getEntityDao() 
	{
		return dao;
	}
	/**
	 * 通过流程发布ID及节点id获取流程设置节点列表
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public List<NodeMessage> getListByActDefIdNodeId(String actDefId,String nodeId)
	{
		return dao.getMessageByActDefIdNodeId(actDefId, nodeId);
	}

	/**
	 * 获取邮件收件人
	 * @param actDefId
	 * @param nodeId
	 * @param execution
	 * @param preTaskUserId
	 * @return
	 */
	public List<ISysUser> getMailReceiver(DelegateExecution execution,Long preTaskUserId){
		return getMessageReceiver(execution, UserCondition.CONDITION_TYPE_MSG_MAIL_RECEIVER, preTaskUserId);
	}
	public List<ISysUser> getMailCopyTo(DelegateExecution execution,Long preTaskUserId){
		return getMessageReceiver(execution, UserCondition.CONDITION_TYPE_MSG_MAIL_COPYTO, preTaskUserId);
	}
	public List<ISysUser> getMailBcc(DelegateExecution execution,Long preTaskUserId){
		return getMessageReceiver(execution, UserCondition.CONDITION_TYPE_MSG_MAIL_BCC, preTaskUserId);
	}
	public List<ISysUser> getInnerReceiver(DelegateExecution execution,Long preTaskUserId){
		return getMessageReceiver(execution, UserCondition.CONDITION_TYPE_MSG_INNER_RECEIVER, preTaskUserId);
	}
	public List<ISysUser> getSmsReceiver(DelegateExecution execution,Long preTaskUserId){
		return getMessageReceiver(execution, UserCondition.CONDITION_TYPE_MSG_MOBILE_RECEIVER,  preTaskUserId);
	}

	public List<NodeMessage> getNodeMessages(String actDefId, String nodeId) {
		return dao.getByActDefIdAndNodeId(actDefId,nodeId);
	}
	
	/**
	 * 获取节点消息接收人
	 * @param execution 
	 * @param type 
	 * @param actDefId
	 * @param nodeId
	 * @param preTaskUserId
	 * @return
	 */
	public List<ISysUser> getMessageReceiver(DelegateExecution execution,Integer type,Long preTaskUserId){
		String actDefId = execution.getProcessDefinitionId();
		String nodeId = execution.getCurrentActivityId();
		Definition definition = definitionService.getByActDefId(actDefId);
		Long defId = definition.getDefId();
		NodeSet nodeSet = nodeSetService.getBySetType(defId, NodeSet.SetType_GloabalForm);
		if(nodeSet==null){
			return null;
		}
		List<UserCondition> bpmUserConditions =null;
		if(type==null){
			type=-1;
		}
		switch (type) {
		case UserCondition.CONDITION_TYPE_MSG_MAIL_RECEIVER:
			bpmUserConditions = userConditionService.getReceiverMailConditions(actDefId, nodeId);	
			break;
		case UserCondition.CONDITION_TYPE_MSG_MAIL_COPYTO:
			bpmUserConditions = userConditionService.getCopyToMailConditions(actDefId, nodeId);	
			break;
		case UserCondition.CONDITION_TYPE_MSG_MAIL_BCC:
			bpmUserConditions = userConditionService.getBccMailConditions(actDefId, nodeId);	
			break;
		case UserCondition.CONDITION_TYPE_MSG_INNER_RECEIVER:
			bpmUserConditions = userConditionService.getReceiverInnerConditions(actDefId, nodeId);	
			break;
		case UserCondition.CONDITION_TYPE_MSG_MOBILE_RECEIVER:
			bpmUserConditions = userConditionService.getReceiverSmsConditions(actDefId, nodeId);	
			break;
		default:
			bpmUserConditions = new ArrayList<UserCondition>();
			break;
		}
		
		List<ISysUser> executors = nodeUserService.getTaskExecutors(execution, bpmUserConditions, preTaskUserId);
		
		return executors;
	}
	
}
