package com.cssrc.ibms.api.sysuser.intf;

import java.util.Map;
import com.cssrc.ibms.api.activity.model.IProcessCmd;

/**
 * IEventUtilService
 * @author liubo
 * @date 2017年2月16日
 */
public interface IEventUtilService{

	/**
	 * 发布节点sql事件
	 * @param actDefId,nodeId,action,dataMap
	 */
	public abstract void publishNodeSqlEvent(String actDefId, String nodeId,
			String action, Map<String, Object> dataMap);

	/**
	 * 触发新的流程事件
	 * @param action,nodeId,processCmd
	 * @return
	 */
	public abstract void publishTriggerNewFlowEvent(String action, String nodeId,
			IProcessCmd processCmd);

}