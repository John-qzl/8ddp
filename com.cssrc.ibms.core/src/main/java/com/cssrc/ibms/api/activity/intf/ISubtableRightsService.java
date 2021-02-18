package com.cssrc.ibms.api.activity.intf;

import com.cssrc.ibms.api.activity.model.ISubtableRights;

public interface ISubtableRightsService{

	/**
	 * 根据流程ID和节点ID获取子表权限配置
	 * 
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public abstract ISubtableRights getByDefIdAndNodeId(String actDefId,
			String nodeId, Long tableId, String parentActDefId);

	/**
	 * 根据流程ID和节点ID获取子表权限配置
	 * 
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public abstract ISubtableRights getByDefIdAndNodeId(String actDefId,
			String nodeId, Long tableId);

	public abstract void delByActDefId(String actDefId);

}