package com.cssrc.ibms.api.activity.intf;

import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.activity.model.IFlowNode;
import com.cssrc.ibms.api.activity.model.INodeSet;

public interface INodeSetService {

	/**
	 * 根据流程设置ID取流程节点设置
	 * 
	 * @param defId
	 * @return
	 */
	public abstract List<? extends INodeSet> getByDefId(Long defId);

	/**
	 * 根据流程设置ID取流程所有的节点设置
	 * 
	 * @param defId
	 * @return
	 */
	public abstract List<? extends INodeSet> getAllByDefId(Long defId);

	/**
	 * 根据流程定义id和节点id获取INodeSet对象。
	 * 
	 * @param defId
	 *            流程定义ID
	 * @param nodeId
	 *            节点ID
	 * @return
	 */
	public abstract INodeSet getByDefIdNodeId(Long defId, String nodeId);

	public abstract INodeSet getByDefIdNodeId(Long defId, String nodeId,
			String parentActDefId);

	/**
	 * 根据流程定义获取流程节点设置对象。
	 * 
	 * @param defId
	 * @return
	 */
	public abstract Map<String, ? extends INodeSet> getMapByDefId(Long defId);

	/**
	 * 通过流程发布ID及节点id获取流程设置节点实体
	 * 
	 * @param deployId
	 * @param nodeId
	 * @return
	 */
	public abstract INodeSet getByActDefIdNodeId(String actDefId, String nodeId);

	public abstract INodeSet getByActDefIdNodeId(String actDefId,
			String nodeId, String parentActDefId);

	/**
	 * 通过流程发布ID及节点id获取流程设置节点实体(不用考虑是否有绑定表单)
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public abstract INodeSet getByMyActDefIdNodeId(String actDefId,
			String nodeId);

	public abstract INodeSet getByMyActDefIdNodeId(String actDefId,
			String nodeId, String parentActDefId);

	/**
	 * 取得某个流程定义中对应的某个节点为汇总节点的配置
	 * 
	 * @param actDefId
	 * @param joinTaskKey
	 * @return
	 */
	public abstract INodeSet getByActDefIdJoinTaskKey(String actDefId,
			String joinTaskKey);

	/**
	 * 根据流程定义Id和 表单类型查询 默认表单和起始表单。
	 * 
	 * @param defId
	 * @param setType
	 *            值为(2，全局表单,3，流程业务表单)
	 * @return
	 */
	public abstract INodeSet getBySetType(Long defId, Short setType);

	public abstract INodeSet getBySetType(Long defId, Short setType,
			String parentActDefId);

	/**
	 * 根据流程定义获取当前的表单数据。
	 * 
	 * @param actDefId
	 * @return
	 */
	public abstract List<? extends INodeSet> getByActDefId(String actDefId);

	/**
	 * 通过定义ID及节点Id更新isJumpForDef字段的设置
	 * 
	 * @param nodeId
	 * @param actDefId
	 * @param isJumpForDef
	 */
	public abstract void updateIsJumpForDef(String nodeId, String actDefId,
			Short isJumpForDef);

	/**
	 * 
	 * @param defId
	 * @param parentActDefId
	 * @return
	 */
	public abstract List<? extends INodeSet> getByDefIdOpinion(Long defId,
			String parentActDefId);

	/**
	 * 根据actdefid 获取在线表单的数据。
	 * 
	 * @param actDefId
	 * @return
	 */
	public abstract List<? extends INodeSet> getOnlineFormByActDefId(
			String actDefId);

	/**
	 * 获取第一个节点，
	 * 
	 * @param actDefId
	 * @param actDefId
	 * @return
	 * @throws Exception
	 */
	public abstract IFlowNode getFirstNodeIdFromCache(String actDefId);

	/**
	 * 通过流程发布ID及节点id获取流程设置节点实体
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public abstract INodeSet getByStartGlobal(Long defId);

	/**
	 * 根据formKey获取关联的NodeSet
	 * 
	 * @param formKey
	 * @return
	 */
	public abstract List<? extends INodeSet> getByFormKey(Long formKey);

	/**
	 * 从 缓存中读取node by actId,nodeId
	 *@author YangBo @date 2016年11月24日下午4:15:06
	 *@param actDefId 流程ID
	 *@param nodeId 节点ID
	 *@return
	 */
	public abstract IFlowNode getNodeByActNodeIdFCache(String actDefId,
			String nodeId);

    public abstract INodeSet getStartNodeSet(Long defId, String actDefId)throws Exception;
	
	
}