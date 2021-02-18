package com.cssrc.ibms.core.flow.service;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.NodeUserDao;
import com.cssrc.ibms.core.flow.dao.UserConditionDao;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.model.UserCondition;


/**
 * <pre>
 * 对象功能:节点下的人员的配置规则   Service类 
 * 开发人员:zhulongchao 
 * </pre>
 */
@Service
public class UserConditionService extends BaseService<UserCondition> {
	@Resource
	private UserConditionDao dao;
	
	@Resource
	private NodeUserDao nodeUserDao;
	


	public UserConditionService() {
	}

	@Override
	protected IEntityDao<UserCondition, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 根据流程set获得条件
	 * 
	 * @param setId
	 * @return
	 */
	public List<UserCondition> getBySetId(Long setId) {
		return dao.getBySetId(setId);
	}

	/**
	 * 通过流程定义ID获得用户设置条件
	 * 
	 * @param actDefId 流程定义ID
	 * @return
	 */
	public List<UserCondition> getByActDefId(String actDefId) {
		return dao.getByActDefId(actDefId);
	}
	
	/**
	 * 保存人员设置和规则设置。
	 * @param bpmUserCondition
	 * @param users
	 */
	public void saveConditionAndUser(UserCondition bpmUserCondition,String users){
		Long conditionId = 0L;
		JSONArray jarray = JSONArray.fromObject(users);		
		if(bpmUserCondition.getId()>0){
			conditionId = bpmUserCondition.getId();
			dao.update(bpmUserCondition);
			//根据条件ID删除组合条件。
			nodeUserDao.delByConditionId(conditionId);
		}
		else{
			conditionId = UniqueIdUtil.genId();
			bpmUserCondition.setId(conditionId);
			dao.add(bpmUserCondition);
		}
		for(Object obj : jarray){
			JSONObject jobject = (JSONObject)obj;
			String cmpIds=jobject.getString("cmpIds");
			
			Long nodeUserId=UniqueIdUtil.genId();
			NodeUser nodeUser = new NodeUser();
			nodeUser.setNodeUserId(nodeUserId);
		
			String assignType= jobject.getString("assignType");
			nodeUser.setAssignType(assignType);
			
			nodeUser.setCmpIds(cmpIds);
			nodeUser.setCmpNames(jobject.getString("cmpNames"));
			Short extractUser = 0;
			if(jobject.has("extractUser"))
				extractUser = Short.parseShort(jobject.getString("extractUser"));
			nodeUser.setExtractUser(extractUser);
			nodeUser.setCompType(Short.parseShort(jobject.getString("compType")));
			nodeUser.setConditionId(conditionId);
			
			nodeUserDao.add(nodeUser);
		}
	}

	/**
	 * 通过流程定义ID获得抄送用户设置条件
	 * 
	 * @param actDefId 流程定义ID
	 * @return
	 */
	public List<UserCondition> getCcByActDefId(String actDefId) {
		return dao.getCcByActDefId(actDefId);
	}
	
	/**
	 * 通过流程定义ID获得抄送用户设置条件
	 * @param actDefId 流程定义ID
	 * @return
	 */
	public List<UserCondition> getByActDefIdAndNodeId(String actDefId,String nodeId) {
		return dao.getByActDefIdAndNodeId(actDefId,nodeId);
	}
	
	/**
	 * 通过流程定义ID和节点ID，获取消息节点的邮件接收人
	 * @param actDefId 流程定义ID
	 * @param nodeId 节点ID
	 * @return
	 */
	public List<UserCondition> getReceiverMailConditions(String actDefId,String nodeId) {
		return dao.getByActDefIdAndNodeIdAndType(actDefId,nodeId,UserCondition.CONDITION_TYPE_MSG_MAIL_RECEIVER);
	}
	
	/**
	 * 通过流程定义ID和节点ID，获取消息节点的邮件抄送人
	 * @param actDefId 流程定义ID
	 * @param nodeId 节点ID
	 * @return
	 */
	public List<UserCondition> getCopyToMailConditions(String actDefId,String nodeId) {
		return dao.getByActDefIdAndNodeIdAndType(actDefId,nodeId,UserCondition.CONDITION_TYPE_MSG_MAIL_COPYTO);
	}
	
	/**
	 * 通过流程定义ID和节点ID，获取消息节点的邮件密送人
	 * @param actDefId 流程定义ID
	 * @param nodeId 节点ID
	 * @return
	 */
	public List<UserCondition> getBccMailConditions(String actDefId,String nodeId) {
		return dao.getByActDefIdAndNodeIdAndType(actDefId,nodeId,UserCondition.CONDITION_TYPE_MSG_MAIL_BCC);
	}
	
	/**
	 * 通过流程定义ID和节点ID，获取消息节点的内部消息接收人
	 * @param actDefId 流程定义ID
	 * @param nodeId 节点ID
	 * @return
	 */
	public List<UserCondition> getReceiverInnerConditions(String actDefId,String nodeId) {
		return dao.getByActDefIdAndNodeIdAndType(actDefId,nodeId,UserCondition.CONDITION_TYPE_MSG_INNER_RECEIVER);
	}
	
	/**
	 * 通过流程定义ID和节点ID，获取消息节点的短信接收人
	 * @param actDefId 流程定义ID
	 * @param nodeId 节点ID
	 * @return
	 */
	public List<UserCondition> getReceiverSmsConditions(String actDefId,String nodeId) {
		return dao.getByActDefIdAndNodeIdAndType(actDefId,nodeId,UserCondition.CONDITION_TYPE_MSG_MOBILE_RECEIVER);
	}
	

	public void delConditionById(Long[] id){
		for(int i=0;i<id.length;i++){
			long conditionId=id[i];
			//BPM_NODE_USER
			nodeUserDao.delByConditionId(conditionId);
			//BPM_USER_CONDITION
			dao.delById(conditionId);
		}
	}
}
